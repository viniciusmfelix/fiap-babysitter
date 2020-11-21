package br.com.fiap.babysitter.domain.exception;

public class EntidadeEmUsoException extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}

}
