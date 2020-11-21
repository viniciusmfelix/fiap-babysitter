package br.com.fiap.babysitter.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.babysitter.domain.model.Execucao;
import br.com.fiap.babysitter.domain.repository.ExecucaoRepository;
import br.com.fiap.babysitter.domain.service.ExecucaoService;

@RestController
@RequestMapping("/execucoes")
public class ExecucaoController {
	
	@Autowired
	private ExecucaoRepository execucaoRepository;
	
	@Autowired
	private ExecucaoService execucaoService;
	
	@GetMapping
	public List<Execucao> index() {
		return execucaoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Execucao show(@PathVariable Long id) {
		return execucaoService.buscarPorId(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Execucao store(@RequestBody @Valid Execucao execucao) {
		return execucaoService.adicionar(execucao);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void destroy(@PathVariable Long id) {
		execucaoService.remover(id);
	}

}
