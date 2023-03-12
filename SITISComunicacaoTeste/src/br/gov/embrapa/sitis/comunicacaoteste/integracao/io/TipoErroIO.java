package br.gov.embrapa.sitis.comunicacaoteste.integracao.io;

public enum TipoErroIO {

	EXECUCAO_COM_SUCESSO(0), 
	ERRO_TIMEOUT(1), 
	ERRO_FALHA_COMUNICACAO_BALANCA(2), 
	ERRO_IRRIGACAO_EM_EXECUCAO(3), 
	ERRO_QUANTIDADE_AGUA_A_SER_IRRIGADA_INCONSCISTENTE(4), 
	ERRO_SEM_ALTERACAO_MASSA_COLUNA_SOLO(5), 
	ERRO_FALHA_FECHAMENTO_VALVULA(6), 
	ERRO_IRRIGACAO_INTERROMPIDA(7),
	ERRO_NENHUMA_IRRIGACAO_EXECUCAO(8),
	ERRO_BALANCA_COM_MAU_FUNCIONAMENTO(9),
	ERRO_OPERACAO_NAO_SUPORTADA(10);

	private int tipo;

	/**
	 * Construtor privado da classe.
	 * 
	 * @param tipo
	 *            Valor inteiro associado a cada tipo de erro válido.
	 */
	private TipoErroIO(int tipo) {
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
	public static TipoErroIO valueOf(int tipo) {
		switch (tipo) {
			case 0:
				return TipoErroIO.EXECUCAO_COM_SUCESSO;
			case 1:
				return TipoErroIO.ERRO_TIMEOUT;
			case 2:
				return TipoErroIO.ERRO_FALHA_COMUNICACAO_BALANCA;
			case 3:
				return TipoErroIO.ERRO_IRRIGACAO_EM_EXECUCAO;
			case 4:
				return TipoErroIO.ERRO_QUANTIDADE_AGUA_A_SER_IRRIGADA_INCONSCISTENTE;
			case 5:
				return TipoErroIO.ERRO_SEM_ALTERACAO_MASSA_COLUNA_SOLO;
			case 6:
				return TipoErroIO.ERRO_FALHA_FECHAMENTO_VALVULA;
			case 7:
				return TipoErroIO.ERRO_IRRIGACAO_INTERROMPIDA;
			case 8:
				return TipoErroIO.ERRO_NENHUMA_IRRIGACAO_EXECUCAO;
			case 9:
				return TipoErroIO.ERRO_BALANCA_COM_MAU_FUNCIONAMENTO;
			case 10:
				return TipoErroIO.ERRO_OPERACAO_NAO_SUPORTADA;
			default:
				return null;
		}
	}
}
