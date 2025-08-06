package br.org.apae.api_crud_pacientes.api.dtos.response;

import java.time.LocalDate;
import java.util.UUID;

public class VacinaResponse {

  private UUID id;
  private String nome;
  private LocalDate dataAplicacao;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public LocalDate getDataAplicacao() {
    return dataAplicacao;
  }

  public void setDataAplicacao(LocalDate dataAplicacao) {
    this.dataAplicacao = dataAplicacao;
  }
}
