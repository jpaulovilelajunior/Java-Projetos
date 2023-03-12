package br.gov.embrapa.sitis.comunicacaoteste.integracao.io;

public enum TipoRequisicaoIO {
	LER_PORTA_ANALOGICA(1), LER_PORTA_DIGITAL(2), ATIVAR_PORTA(3), DESATIVAR_PORTA(
			4), ATIVAR_VALVULA(5), DESATIVAR_VALVULA(6), LER_MASSA(7), IRRIGAR(
			8), INTERROMPER_IRRIGACAO(9);

	private int tipo;

	/**
	 * Construtor privado da classe.
	 * 
	 * @param tipo
	 *            Valor inteiro associado a cada tipo de requisição válido.
	 */
	private TipoRequisicaoIO(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * Recupera o valor inteiro associado à constante enum.
	 * 
	 * @return O valor inteiro associado à constante enum.
	 */
	public int getValor() {
		return this.tipo;
	}

	/**
	 * Recupera a constante enum dessa classe a partir do valor inteiro
	 * fornecido. Caso não haja correspondência a nenhuma constante, é retornado
	 * null.
	 * 
	 * @param tipo
	 *            Valor inteiro associado à constante enum que se deseja
	 *            recuperar.
	 * @return Constante enum associado ao valor inteiro fornecido.
	 */
	public static TipoRequisicaoIO valueOf(int tipo) {
		switch (tipo) {
			case 1:
				return TipoRequisicaoIO.LER_PORTA_ANALOGICA;
			case 2:
				return TipoRequisicaoIO.LER_PORTA_DIGITAL;
			case 3:
				return TipoRequisicaoIO.ATIVAR_PORTA;
			case 4:
				return TipoRequisicaoIO.DESATIVAR_PORTA;
			case 5:
				return TipoRequisicaoIO.ATIVAR_VALVULA;
			case 6:
				return TipoRequisicaoIO.DESATIVAR_VALVULA;
			case 7:
				return TipoRequisicaoIO.LER_MASSA;
			case 8:
				return TipoRequisicaoIO.IRRIGAR;
			case 9:
				return TipoRequisicaoIO.INTERROMPER_IRRIGACAO;
			default:
				return null;
		}
	}
}
