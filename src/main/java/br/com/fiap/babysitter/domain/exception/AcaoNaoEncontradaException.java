package br.com.fiap.babysitter.domain.exception;

public class AcaoNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public AcaoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
