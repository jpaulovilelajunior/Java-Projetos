package br.gov.embrapa.sitis.comunicacaoteste.integracao.io;

public class MensagemIO {

	public static final String DELIMITADOR_CAMPO = ",";
	public static final String DELIMITADOR_MSG = "@";

	private long requisicaoId;
	private TipoErroIO codigoErro;
	private TipoRequisicaoIO operacao;
	private String modulo;
	private int porta;
	private double valor;

	public MensagemIO() {
		super();
	}

	public MensagemIO(long requisicaoId, TipoErroIO erro, TipoRequisicaoIO operacao, String modulo,
			int porta, double valor) {
		super();
		this.requisicaoId = requisicaoId;
		this.codigoErro = erro;
		this.operacao = operacao;
		this.modulo = modulo;
		this.porta = porta;
		this.valor = valor;
	}

	public MensagemIO(String msg) throws Exception {
		super();
		this.setAtributos(msg);
	}

//	public MensagemIO(RequisicaoIO requisicao) {
//		super();
//		this.setAtributos(requisicao);
//	}

	public long getRequisicaoId() {
		return requisicaoId;
	}

	public void setRequisicaoId(long requisicaoId) {
		this.requisicaoId = requisicaoId;
	}

	public TipoErroIO getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(TipoErroIO codigoErro) {
		this.codigoErro = codigoErro;
	}

	public TipoRequisicaoIO getOperacao() {
		return operacao;
	}

	public void setOperacao(TipoRequisicaoIO operacao) {
		this.operacao = operacao;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public void setAtributos(int[] msg) throws Exception {

		// convertendo o array de inteiros em string
		String mensagem = new String(msg, 0, msg.length);

		// obtendo apenas a parte especifica da mensagem recebida do SITIS IO
		mensagem = mensagem.substring(16, mensagem.length() - 1);

		// recuperando os campos da mensagem de IO
		this.setAtributos(mensagem);
	}

	public void setAtributos(String msg) throws Exception {

		if (msg.endsWith(DELIMITADOR_MSG)) {

			// removendo o delimitador de fim de mensagem
			msg = msg.substring(0, msg.length() - 1);

			// recuperando os campos da mensagem de IO
			String[] campos = msg.split(MensagemIO.DELIMITADOR_CAMPO);
			this.requisicaoId = Long.valueOf(campos[0]);
			this.codigoErro = TipoErroIO.valueOf(Integer.valueOf(campos[1]));
			this.operacao = TipoRequisicaoIO.valueOf(Integer.valueOf(campos[2]));
			this.modulo = campos[3];
			this.porta = Integer.parseInt(campos[4]);
			this.valor = Double.valueOf(campos[5].trim());
		}

	}

	// public void setAtributos(RequisicaoIO requisicao) {
	//
	// // recuperando os campos da mensagem de IO
	// this.requisicaoId = requisicao.getId();
	// this.codigoErro = TipoErroIO.EXECUCAO_COM_SUCESSO;
	// this.operacao = requisicao.getTipo();
	// this.modulo = requisicao.getModulo();
	// this.porta = requisicao.getPorta();
	// this.valor = (double) requisicao.getValor();
	// }

	public String toString() {
		String msg = this.getRequisicaoId() + MensagemIO.DELIMITADOR_CAMPO + this.getCodigoErro().getValor() + MensagemIO.DELIMITADOR_CAMPO + this.getOperacao().getValor() + MensagemIO.DELIMITADOR_CAMPO + this.getModulo() + MensagemIO.DELIMITADOR_CAMPO + this.getPorta() + MensagemIO.DELIMITADOR_CAMPO + this.getValor() + MensagemIO.DELIMITADOR_CAMPO + MensagemIO.DELIMITADOR_MSG;
		return msg;
	}

	public int[] getMensagem() {

		// recuperando a string com os atributos da mensagem
		char[] charArray = this.toString().toCharArray();

		// convertendo os caracteres num vetor de inteiros
		int[] intArray = new int[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			intArray[i] = (int) charArray[i];
		}

		// retornando o vetor de inteiros com os caracteres dos atributos da
		// mensagem
		return intArray;
	}

	public Object clone() {
		return new MensagemIO(this.getRequisicaoId(), this.getCodigoErro(), this.getOperacao(), this.getModulo(), this.getPorta(), this.getValor());
	}
}
