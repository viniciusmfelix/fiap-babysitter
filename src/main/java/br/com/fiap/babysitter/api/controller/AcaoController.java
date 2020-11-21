package br.com.fiap.babysitter.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.babysitter.domain.model.Acao;
import br.com.fiap.babysitter.domain.repository.AcaoRepository;
import br.com.fiap.babysitter.domain.service.AcaoService;

@RestController
@RequestMapping("/acoes")
public class AcaoController {
	
	@Autowired
	private AcaoRepository acaoRepository;
	
	@Autowired
	private AcaoService acaoService;
	
	@GetMapping
	public List<Acao> index() {
		return acaoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Acao show(@PathVariable Long id) {
		return acaoService.buscarPorId(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Acao store(@RequestBody @Valid Acao acao) {
		return acaoService.salvar(acao);
	}
	
	@PutMapping("/{id}")
	public Acao update(@PathVariable Long id, @RequestBody @Valid Acao acao) {
		Acao acaoAtual = acaoService.buscarPorId(id);
		BeanUtils.copyProperties(acao, acaoAtual, "id", "execucoes");
		
		return acaoAtual = acaoService.salvar(acaoAtual);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void destroy(@PathVariable Long id) {
		acaoService.remover(id);
	}

}
