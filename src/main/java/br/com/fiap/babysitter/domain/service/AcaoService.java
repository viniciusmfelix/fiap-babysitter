package br.com.fiap.babysitter.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.fiap.babysitter.domain.exception.AcaoNaoEncontradaException;
import br.com.fiap.babysitter.domain.exception.EntidadeEmUsoException;
import br.com.fiap.babysitter.domain.exception.NegocioException;
import br.com.fiap.babysitter.domain.model.Acao;
import br.com.fiap.babysitter.domain.repository.AcaoRepository;

@Service
public class AcaoService {
	
	private static final String MSG_ACAO_EM_USO = "A��o de ID %d n�o pode ser removida pois est� associada a uma ou mais execu��es.";

	private static final String MSG_ACAO_NAO_ENCONTRADA = "A��o de ID %d n�o encontrada no registro de a��es.";
	
	@Autowired
	private AcaoRepository acaoRepository;
	
	public Acao buscarPorId(Long id) {
		return acaoRepository.findById(id)
				.orElseThrow(() -> new AcaoNaoEncontradaException(String.format(MSG_ACAO_NAO_ENCONTRADA, id)));
	}
	
	public Acao salvar(Acao acao) {
		try {
			return acaoRepository.save(acao);
		} catch(DataIntegrityViolationException exception) {
			throw new NegocioException("A propriedade 'nome' deve ser especificada com um valor v�lido.");
		}
	}
	
	public void remover(Long id) {
		try {
			acaoRepository.deleteById(id);
		} catch(EmptyResultDataAccessException exception) {
			throw new AcaoNaoEncontradaException(String.format(MSG_ACAO_NAO_ENCONTRADA, id));
		} catch(DataIntegrityViolationException exception) {
			throw new EntidadeEmUsoException(String.format(MSG_ACAO_EM_USO, id));
		}
	}

}
