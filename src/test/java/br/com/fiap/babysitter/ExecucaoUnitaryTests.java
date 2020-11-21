package br.com.fiap.babysitter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.babysitter.domain.model.Acao;
import br.com.fiap.babysitter.domain.model.Execucao;

class ExecucaoUnitaryTests {
	
	Acao acao = new Acao();
	Execucao execucao = new Execucao();
	
	@BeforeEach
	public void setUp() {
		acao.setId(1L);
		acao.setNome("Ir ao mercado");
	}

	@Test
	void cadastrarExecucaoComAcaoAtiva() {
		acao.setAtivo(true);
		
		if(acao.isAtivo()) {
			execucao.setAcao(acao);
		} else {
			execucao.setAcao(null);
		}
		
		assertEquals(execucao.getAcao().getId(), 1L);
	}
	
	@Test
	void cadastrarExecucaoComAcaoNaoAtiva() {
		acao.setAtivo(false);
		
		if(acao.isAtivo()) {
			execucao.setAcao(acao);
		} else {
			execucao.setAcao(null);
		}
		
		assertEquals(execucao.getAcao(), null);
	}

}
