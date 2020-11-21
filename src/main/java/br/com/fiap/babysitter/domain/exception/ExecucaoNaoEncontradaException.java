package br.com.fiap.babysitter.domain.exception;

public class ExecucaoNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public ExecucaoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

}
