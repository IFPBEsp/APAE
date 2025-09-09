package br.org.apae.api_crud_pacientes.domain.model;

import br.org.apae.api_crud_pacientes.domain.model.pessoa.Pessoa;

import java.util.UUID;

public class TipoDeficiencia {

  private UUID id;
  private String descricao;

  public TipoDeficiencia() {}

  public TipoDeficiencia(UUID id, String descricao) {
    this.id = id;
    this.descricao = descricao;
  }

  public TipoDeficiencia(String descricao, Pessoa pessoa) {
    this.descricao = descricao;
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

}
