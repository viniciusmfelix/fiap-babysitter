package br.com.fiap.babysitter.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.fiap.babysitter.domain.exception.AcaoNaoAtivaException;
import br.com.fiap.babysitter.domain.exception.EntidadeEmUsoException;
import br.com.fiap.babysitter.domain.exception.EntidadeNaoEncontradaException;
import br.com.fiap.babysitter.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception exception, WebRequest request) {
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		TipoProblema tipoProblema = TipoProblema.ERRO_INTERNO;
		
		String detalhes = exception.getMessage();
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		
		return handleExceptionInternal(exception, problema, new HttpHeaders(), status, request);
		
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocioException(NegocioException exception, WebRequest request) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		TipoProblema tipoProblema = TipoProblema.ERRO_NEGOCIO;
		
		String detalhes = exception.getMessage();
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		
		return handleExceptionInternal(exception, problema, new HttpHeaders(), status, request);
		
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException exception, WebRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		TipoProblema tipoProblema = TipoProblema.RECURSO_NAO_ENCONTRADO;
		
		String detalhes = exception.getMessage();
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		
		return handleExceptionInternal(exception, problema, new HttpHeaders(), status, request);
		
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<Object> handleEntidadeEmUsoException(EntidadeEmUsoException exception, WebRequest request) {
		
		HttpStatus status = HttpStatus.CONFLICT;
		TipoProblema tipoProblema = TipoProblema.ENTIDADE_EM_USO;
		String detalhes = exception.getMessage();
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		
		return handleExceptionInternal(exception, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(AcaoNaoAtivaException.class)
	public ResponseEntity<Object> handleAcaoNaoAtivaException(AcaoNaoAtivaException exception, WebRequest request) {
		
		HttpStatus status = HttpStatus.CONFLICT;
		TipoProblema tipoProblema = TipoProblema.ACAO_INATIVA;
		
		String detalhes = exception.getMessage();
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		
		return handleExceptionInternal(exception, problema, new HttpHeaders(), status, request);
		
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		BindingResult bindingResult = exception.getBindingResult();
		
		List<Campo> campos = (List<Campo>) bindingResult.getFieldErrors().stream()
				.map(campoErro -> {
					String mensagem = messageSource.getMessage(campoErro, LocaleContextHolder.getLocale());
					
					Campo campoProblema = new Campo();
					campoProblema.setNome(campoErro.getField());
					campoProblema.setMensagem(mensagem);
					
					return campoProblema;
				}).collect(Collectors.toList());
		
		status = HttpStatus.BAD_REQUEST;
		String detalhes = "O corpo da requisição está inválido. Cheque os dados e tente novamente.";
		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		problema.setCampos(campos);
		
		return handleExceptionInternal(exception, problema, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(exception);
		
		if(rootCause instanceof InvalidFormatException) {
			return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		}
		
		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detalhes = "O corpo da requisicao esta inválido. Verifique o erro de sintaxe e tente novamente.";
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);
		
		return handleExceptionInternal(exception, problema, headers, status, request);
	}
	
	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		
		String path = exception.getPath().stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
		
		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detalhes = String.format("A propriedade '%s' recebeu o valor '%s' que e de um tipo invalido: '%s', corrija e informe um valor compativel com o tipo %s."
				, path, exception.getValue(), exception.getValue().getClass().getSimpleName(), exception.getTargetType().getSimpleName());
		
		Problema problema = problemaBuilder(status, tipoProblema, detalhes);

		
		return handleExceptionInternal(exception, problema, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if(body == null) {
			Problema problema = new Problema();
			problema.setTimestamp(OffsetDateTime.now());
			problema.setDetalhes(exception.getMessage());
			problema.setStatus(status.value());
			problema.setTipo(status.getReasonPhrase());
			
			body = problema;
		}
		
		return super.handleExceptionInternal(exception, body, headers, status, request);
	}
	
	private Problema problemaBuilder(HttpStatus status, TipoProblema tipoProblema, String detalhes) {
		Problema problema = new Problema();
		
		problema.setTimestamp(OffsetDateTime.now());
		problema.setDetalhes(detalhes);
		problema.setTipo(tipoProblema.getUri());
		problema.setTitulo(tipoProblema.getTitulo());
		problema.setStatus(status.value());
		
		problema.setTimestamp(OffsetDateTime.now());
		problema.setDetalhes(detalhes);
		
		return problema;
	}
 
}
