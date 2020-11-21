package br.com.fiap.babysitter.api.exceptionhandler;

public enum TipoProblema {
	
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado."),
	DADOS_INVALIDOS("/dados-invalidos", "Erro de requisição."),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-ilegivel", "Mensagem incompreensivel"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro de requisição inválido."),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso."),
	ACAO_INATIVA("/acao-inativa", "Ação inativa no sistema."),
	ERRO_INTERNO("/erro-interno", "Erro interno do servidor"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");
	
	private String titulo;
	private String uri;
	
	TipoProblema(String path, String uri) {
		this.titulo = "http://api.babysitter.fiap" + path;
		this.uri = uri;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
