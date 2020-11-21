package br.com.fiap.babysitter.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.babysitter.domain.model.Acao;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Long> {

}
