package br.com.fiap.babysitter.domain.model;


import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import br.com.fiap.babysitter.api.core.validation.Groups;

@Entity
@Table(name = "execucao")
public class Execucao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXECUCAO_SEQ")
	@SequenceGenerator(sequenceName = "execucao_seq", allocationSize = 1, name = "EXECUCAO_SEQ")
	private Long id;
	
	@Valid
	@ConvertGroup(from = Default.class, to = Groups.AcaoId.class)
	@ManyToOne
	private Acao acao;
	
	@CreationTimestamp
	@Column(name = "data_exec")
	private OffsetDateTime dataExecucao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public OffsetDateTime getDataExecucao() {
		return dataExecucao;
	}

	public void setDataExecucao(OffsetDateTime dataExecucao) {
		this.dataExecucao = dataExecucao;
	}

}
