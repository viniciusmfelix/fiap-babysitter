package br.com.fiap.babysitter.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.fiap.babysitter.domain.exception.AcaoNaoAtivaException;
import br.com.fiap.babysitter.domain.exception.AcaoNaoEncontradaException;
import br.com.fiap.babysitter.domain.exception.ExecucaoNaoEncontradaException;
import br.com.fiap.babysitter.domain.model.Acao;
import br.com.fiap.babysitter.domain.model.Execucao;
import br.com.fiap.babysitter.domain.repository.AcaoRepository;
import br.com.fiap.babysitter.domain.repository.ExecucaoRepository;

@Service
public class ExecucaoService {
	
	private static final String MSG_EXECUCAO_NAO_ENCONTRADA = "Execu��o de ID %d n�o encontrada no registro de execu��es.";
	
	private static final String MSG_ACAO_NAO_ENCONTRADA = "A��o de ID %d n�o encontrada no registro de a��es.";
	
	@Autowired
	private ExecucaoRepository execucaoRepository;
	
	@Autowired
	private AcaoRepository acaoRepository;
	
	public Execucao buscarPorId(Long id) {
		return execucaoRepository.findById(id)
				.orElseThrow(() -> new ExecucaoNaoEncontradaException(String.format(MSG_EXECUCAO_NAO_ENCONTRADA, id)));
	}
	
	public Execucao adicionar(Execucao execucao) {
		Long acaoId = execucao.getAcao().getId();
		
		Acao acao = acaoRepository.findById(acaoId)
				.orElseThrow(() -> new AcaoNaoEncontradaException(String.format(MSG_ACAO_NAO_ENCONTRADA, acaoId)));
		
		if(!acao.isAtivo()) {
			throw new AcaoNaoAtivaException(String.format("Execu��o n�o pode ser performada pois a a��o de ID %d associada n�o se encontra ativa no sistema.", acaoId));
		}
		
		execucao.setAcao(acao);
		
		return execucaoRepository.save(execucao);
	}
	
	public void remover(Long id) {
		try {
			execucaoRepository.deleteById(id);
		} catch(EmptyResultDataAccessException exception) {
			throw new ExecucaoNaoEncontradaException(String.format(MSG_EXECUCAO_NAO_ENCONTRADA, id));
		}
	}

}
