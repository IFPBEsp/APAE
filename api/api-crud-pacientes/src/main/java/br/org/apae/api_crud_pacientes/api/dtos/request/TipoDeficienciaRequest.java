package br.org.apae.api_crud_pacientes.api.dtos.request;

import java.util.UUID;

public class TipoDeficienciaRequest {
  private String descricao;
  public UUID pessoaId;

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public UUID getPessoaId() {
    return pessoaId;
  }

  public void setPessoaId(UUID pessoaId) {
    this.pessoaId = pessoaId;
  }
}
