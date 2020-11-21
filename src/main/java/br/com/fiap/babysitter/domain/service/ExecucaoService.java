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
	
	private static final String MSG_EXECUCAO_NAO_ENCONTRADA = "Execução de ID %d não encontrada no registro de execuções.";
	
	private static final String MSG_ACAO_NAO_ENCONTRADA = "Ação de ID %d não encontrada no registro de ações.";
	
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
			throw new AcaoNaoAtivaException(String.format("Execução não pode ser performada pois a ação de ID %d associada não se encontra ativa no sistema.", acaoId));
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
