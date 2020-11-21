package br.com.fiap.babysitter.domain.model;	

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;

import br.com.fiap.babysitter.api.core.validation.Groups;

@Entity
@Table(name = "acao")
public class Acao {
	
	@NotNull(groups = Groups.AcaoId.class, message = "O ID de uma ação deve ser informado.")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACAO_SEQ")
	@SequenceGenerator(sequenceName = "acao_seq", allocationSize = 1, name = "ACAO_SEQ")
	private Long id;
	
	@Size(max = 40, message = "O campo de nome não deve possuir mais do que 40 caracteres!")
	@NotBlank(groups = Default.class, message = "O campo de nome não pode ser nulo, vazio ou em branco!")
	@Column(name = "nome")
	private String nome;
	
	@Size(max = 100, message = "O campo de descrição não deve possuir mais do que 100 caracteres!")
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "ativo")
	private boolean ativo;
	
	@JsonIgnore
	@OneToMany(mappedBy = "acao")
	private List<Execucao> execucoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Execucao> getExecucoes() {
		return execucoes;
	}

	public void setExecucoes(List<Execucao> execucoes) {
		this.execucoes = execucoes;
	}

}
