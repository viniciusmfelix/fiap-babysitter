package br.com.fiap.babysitter.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.babysitter.domain.model.Execucao;

@Repository
public interface ExecucaoRepository extends JpaRepository<Execucao, Long> {

}
