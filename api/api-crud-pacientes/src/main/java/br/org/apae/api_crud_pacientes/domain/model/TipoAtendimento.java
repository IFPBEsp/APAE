package br.org.apae.api_crud_pacientes.domain.model;

import java.util.UUID;

public class TipoAtendimento {

  private UUID id;
  private String descricao;
  private Pessoa pessoa;

  public TipoAtendimento() {}

  public TipoAtendimento(UUID id, String descricao, Pessoa pessoa) {
    this.id = id;
    this.descricao = descricao;
    this.pessoa = pessoa;
  }

  public TipoAtendimento(String descricao, Pessoa pessoa) {
    this.descricao = descricao;
    this.pessoa = pessoa;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }
}
