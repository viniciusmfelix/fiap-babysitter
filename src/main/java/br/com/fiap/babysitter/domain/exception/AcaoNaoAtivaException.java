package br.com.fiap.babysitter.domain.exception;

public class AcaoNaoAtivaException extends NegocioException {

	private static final long serialVersionUID = 1L;
	
	public AcaoNaoAtivaException(String mensagem) {
		super(mensagem);
	}

}
