package br.gov.embrapa.sitis.comunicacaoteste;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.gov.embrapa.sitis.comunicacaoteste.integracao.io.MensagemIO;

public class ModuloIOVirtual implements Runnable {

	// endereco IP e porta TCP da Unidade de Controle e Gateway
	private static final int PORTA_TCP_UNIDADE_CONTROLE = 8080;
	public static final int PORTA_TCP_GATEWAY = 2000;
	private String enderecoIPUnidadeControle;
	public String enderecoIPGateway;

	// servidor Socket
	private ServerSocket servidorSocketsGatewayIO;

	// atributos da mensagem trafegada na rede wi-fi
	private MensagemIO mensagemIO;

	// thread do gateway
	private Thread processamentoMensagens;
	private boolean threadAtiva;

	private String cabecalho = "ModuloIOVirtual - ";

	public ModuloIOVirtual(String enderecoIPUnidadeControle, String enderecoIPGateway) {
		super();

		// criando instancia de mensagemIO
		mensagemIO = new MensagemIO();

		// criando o endereco IP da Unidae de Controle e do gateway
		this.enderecoIPUnidadeControle = enderecoIPUnidadeControle;
		this.enderecoIPGateway = enderecoIPGateway;

		// configurando a ativação da Thread
		this.threadAtiva = true;

		// criando a thread para processamento das mensagens oriundas do SITISControle
		this.processamentoMensagens = new Thread(this);
		this.processamentoMensagens.setName("ModuloIOVirtual-" + this.enderecoIPGateway);
		this.processamentoMensagens.start();

		// criando o servidor de sockets para receber as mensagem do ComunicacaoTeste
		try {
			this.servidorSocketsGatewayIO = new ServerSocket(ComunicacaoTeste.PORTA_TCP_GATEWAY);

			System.out.println(cabecalho + "Iniciado servidor socket em ModuloIOVirtual na porta TCP/IP: " + Integer.toString(ComunicacaoTeste.PORTA_TCP_GATEWAY));

		} catch (IOException e) {

			// escrevendo as informacoes de falha na criacao do servidor de sockets no log
			System.out.println(cabecalho + "Erro ao criar o servidor de sockets usado para receber mensagens de ComunicacaoTeste: " + e.getMessage());

			this.finalizar();
			return;
		}

	}

	public void finalizar() {

		try {

			// encerrando o servidor de sockets
			if (this.servidorSocketsGatewayIO != null) {
				this.servidorSocketsGatewayIO.close();
			}

		} catch (IOException e) {

			// escrevendo as informacoes de falha no encerramento do servidor de sockets no log
			System.out.println(cabecalho + ";Erro ao encerrar o servidor de sockets usado para receber mensagens de ComunicacaoTeste: " + e.getMessage());

		}

	}

	@Override
	public void run() {

		// inicializalizando variaveis
		BufferedReader bufferRecebimento;
		Socket conexaoGatewayIO;

		// laco infinito
		while (this.threadAtiva) {

			try {
				if (this.servidorSocketsGatewayIO != null) {

					// recebendo novas conexoes dos modulos de IO
					conexaoGatewayIO = this.servidorSocketsGatewayIO.accept();

					// inicializalizando variaveis
					bufferRecebimento = null;

					// criando o stream de entrada de dados
					bufferRecebimento = new BufferedReader(new InputStreamReader(conexaoGatewayIO.getInputStream()));

					// aguardando o recebimento da resposta
					String mensagem = bufferRecebimento.readLine();

					// mostrando mensagem recebida
					System.out.println(cabecalho + "Mensagem recebida: " + mensagem);

					// recuperando a mensagem da resposta
					this.mensagemIO.setAtributos(mensagem);

					// enviando mensagem de resposta
					this.enviarResposta(this.mensagemIO);

				}

			} catch (Exception e) {

				// erro no recebimento da mensagem via Socket
				System.out.println(cabecalho + "Erro ao ler mensagem via Socket: " + e.getMessage());
			}

		}

	}

	public void enviarResposta(MensagemIO respostaIO) {

		// inicilizalizando variaveis
		Socket socketModuloIO = null;
		PrintStream bufferEnvio = null;

		// adicionando 1 no valor da resposta
		respostaIO.setValor(respostaIO.getValor() + 1);

		// formatando a mensagem de resposta para uma string
		String mensagemResposta = respostaIO.toString();

		try {

			// criando o socket para conexao com o modulo de IO
			socketModuloIO = new Socket(this.enderecoIPUnidadeControle, this.PORTA_TCP_UNIDADE_CONTROLE);

			// criando o stream de saida de dados
			bufferEnvio = new PrintStream(socketModuloIO.getOutputStream());

			// enviando a mensagem
			bufferEnvio.println(mensagemResposta);

			// resposta envidada
			System.out.println(cabecalho + "Resposta enviada: " + mensagemResposta);

		} catch (Exception e) {

			// escrevendo as informacoes de falha no envio da mensagem no log
			System.out.println(cabecalho + "Erro ao enviar mensagem via Socket para SitisControle para o endereço IP: " + this.enderecoIPUnidadeControle + " Porta:" + ComunicacaoTeste.PORTA_TCP_UNIDADE_CONTROLE + " " + e.getMessage());

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

				// escrevendo as informacoes de erro no encerramento do
				// socket para conexao com o modulo de IO no log
				System.out.println(cabecalho + "Erro ao encerrar o socket usado para envio da mensagem para SitisControle para o endereco IP: " + this.enderecoIPUnidadeControle + " Porta:" + ComunicacaoTeste.PORTA_TCP_UNIDADE_CONTROLE + " " + e.getMessage());
			}
		}

		return;

	}

	public void setThreadAtiva(boolean threadAtiva) {
		this.threadAtiva = threadAtiva;
	}
}
