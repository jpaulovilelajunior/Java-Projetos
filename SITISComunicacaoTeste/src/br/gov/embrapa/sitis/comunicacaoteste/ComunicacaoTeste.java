package br.gov.embrapa.sitis.comunicacaoteste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import br.gov.embrapa.sitis.comunicacaoteste.integracao.io.MensagemIO;
import br.gov.embrapa.sitis.comunicacaoteste.integracao.io.TipoErroIO;
import br.gov.embrapa.sitis.comunicacaoteste.integracao.io.TipoRequisicaoIO;

public class ComunicacaoTeste {

	public static final String NOME_THREAD_PROCESSAMENTO_CONEXOES_MODULOS_IO = "SitisControleIO-ModulosIO";

	// valores padrão utilizados na requisicao
	public static final String ENDERECO_IP_UNIDADE_CONTROLE = "10.144.121.64";
	public static final int PORTA_TCP_UNIDADE_CONTROLE = 8080;
	public static final String ENDERECO_IP_GATEWAY = "10.144.121.248";
	public static final int PORTA_TCP_GATEWAY = 2000;

	private Thread processamentoConexoesModulosIO;
	private ServerSocket servidorSocketsModulosIO;
	// private static MensagemIO mensagemIO = new MensagemIO();

	private String cabecalho = "ComunicacaoTeste - ";

	// instancia de moduloIOVirtual para responder as mensagens solicitadas
	private static ModuloIOVirtual moduloIOVirtual;

	private static boolean threadAtiva;

	public ComunicacaoTeste() {
		super();

		// criando o servidor de sockets para receber as respostas dos modulos de IO
		try {
			this.servidorSocketsModulosIO = new ServerSocket(ComunicacaoTeste.PORTA_TCP_UNIDADE_CONTROLE);

			System.out.println(cabecalho + "Iniciado servidor socket em ComunicacaoTeste na porta TCP/IP: " + Integer.toString(ComunicacaoTeste.PORTA_TCP_UNIDADE_CONTROLE));

		} catch (IOException e) {
			System.out.println(cabecalho + e.getMessage());
		}

		// configurando a ativação da Thread
		ComunicacaoTeste.threadAtiva = true;

		// criando a thread para receber conexoes dos modulos de IO (socketServer)
		this.processamentoConexoesModulosIO = new Thread(new ComunicacaoTeste.ProcessamentoConexoesModulosIO(this));
		this.processamentoConexoesModulosIO.setName(ComunicacaoTeste.NOME_THREAD_PROCESSAMENTO_CONEXOES_MODULOS_IO);
		this.processamentoConexoesModulosIO.start();

		// criando moduloIOVirtual
		ComunicacaoTeste.moduloIOVirtual = new ModuloIOVirtual(ComunicacaoTeste.ENDERECO_IP_UNIDADE_CONTROLE, ComunicacaoTeste.ENDERECO_IP_GATEWAY);

	}

	public static void main(String[] args) {

		ComunicacaoTeste comunicacaoTeste = new ComunicacaoTeste();

		// valores que serao inseridos na(s) requisicao(oes)
		long quantidadeDeRequisicoes = 15;
		TipoRequisicaoIO operacao = TipoRequisicaoIO.LER_MASSA;
		String modulo = "1A011";
		int porta = 0;

		System.out.println(comunicacaoTeste.cabecalho + "Iniciando envio das mensagens");
		System.out.println("===================================================");
		System.out.println();

		// enviar mensagem(ns)
		for (long i = 1; i <= quantidadeDeRequisicoes; i++) {

			String mensagem = new MensagemIO(i, TipoErroIO.EXECUCAO_COM_SUCESSO, operacao, modulo, porta, (double) i).toString();
			comunicacaoTeste.enviarMensagem(mensagem);

		}

		// laco para esperar a(s) resposta(s)
		Date agora = new Date();

		// o tempo de espera expresso em segundos
		int temporEspera = 30;

		// obtendo a data termino a esperar
		Date termino = new Date(agora.getTime() + (temporEspera));
		System.out.println("agora: " + agora + " - " + agora.getTime() + "    termino: " + termino + " - " + termino.getTime());
		comunicacaoTeste.esperar(termino);

		// interrompendo Threads
		comunicacaoTeste.moduloIOVirtual.setThreadAtiva(false);
		ComunicacaoTeste.threadAtiva = false;

	}

	private void enviarMensagem(String mensagem) {

		// inicializando variaveis
		Socket socketModuloIO = null;
		PrintStream bufferEnvio = null;

		try {

			// criando o socket para conexao com o modulo de IO
			socketModuloIO = new Socket(ComunicacaoTeste.ENDERECO_IP_GATEWAY, ComunicacaoTeste.PORTA_TCP_GATEWAY);

			// criando o stream de saida de dados
			bufferEnvio = new PrintStream(socketModuloIO.getOutputStream());

			// enviando a mensagem
			bufferEnvio.println(mensagem);

			// mensagem enviada com sucesso
			System.out.println(cabecalho + "Mensagem " + mensagem + " enviada ao moduloIOVirtual: " + ComunicacaoTeste.ENDERECO_IP_GATEWAY + " - " + ComunicacaoTeste.PORTA_TCP_GATEWAY);

		} catch (Exception e) {

			System.out.println(cabecalho + "Ocorreu uma exceção no envio da mensagem '" + mensagem + "' - " + e.getMessage());

		} finally {

			try {

				// encerrando o stream de saida de dados
				if (bufferEnvio != null) {
					bufferEnvio.close();
				}

				// encerrando o socket para conexao com o modulo de IO
				if (socketModuloIO != null) {
					socketModuloIO.close();
				}

			} catch (Exception e) {

				System.out.println(cabecalho + "Ocorreu uma exceção na finalização do envio da mensagem '" + mensagem + e.getMessage());
			}
		}

	}

	private void esperar(Date dataTermino) {

		// verificando se chegou o tempo fim
		while (new Date().before(dataTermino)) {
			// nada a fazer
		}
	}

	private class ProcessamentoConexoesModulosIO implements Runnable {

		private ComunicacaoTeste comunicacaoTeste;

		public ProcessamentoConexoesModulosIO(ComunicacaoTeste comunicacaoTeste) {

			// inicializando atributos de instancia
			this.comunicacaoTeste = comunicacaoTeste;
		}

		@Override
		public void run() {

			// laco infinito
			while (comunicacaoTeste.threadAtiva) {

				// // verificando se a thread esta desativada ou nao.
				// while (!comunicacaoTeste.threadAtiva) {
				// try {
				// synchronized (this) {
				// this.wait();
				// }
				// } catch (Exception e) {
				// System.out.println(cabecalho + e.getMessage());
				// }
				// }

				// recebendo novas conexoes dos modulos de IO
				try {
					Socket conexaoModuloIO = this.comunicacaoTeste.servidorSocketsModulosIO.accept();

					// inicilizalizando variaveis
					BufferedReader bufferRecebimento = null;

					try {

						// criando o stream de entrada de dados
						bufferRecebimento = new BufferedReader(new InputStreamReader(conexaoModuloIO.getInputStream()));

						// aguardando o recebimento da resposta
						String mensagemResposta = bufferRecebimento.readLine();

						// mostrar a mensagem de resposta recebida.
						System.out.println(cabecalho + "Resposta recebida: " + mensagemResposta);
						System.out.println();

					} catch (Exception e) {

						System.out.println(cabecalho + "Ocorreu uma exceção na recepção da resposta de mensagem: " + e.getMessage());

					} finally {

						try {

							// encerrando o stream de saida de dados
							if (bufferRecebimento != null) {
								bufferRecebimento.close();
							}

							// encerrando o socket para conexao com o modulo de IO
							if (conexaoModuloIO != null) {
								conexaoModuloIO.close();
							}

						} catch (Exception e) {

							System.out.println(cabecalho + "Ocorreu uma exceção na finalização da resposta de mensagem: " + e.getMessage());

						}
					}

				} catch (IOException e) {
					System.out.println(cabecalho + "Ocorreu uma exceção no 'accept' da conexão socket: " + e.getMessage());
				}

			}

			System.out.println(cabecalho + "fim da Thread");

		}
	}

}
