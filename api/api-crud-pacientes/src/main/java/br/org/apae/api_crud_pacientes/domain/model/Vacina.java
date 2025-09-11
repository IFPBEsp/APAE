package br.org.apae.api_crud_pacientes.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class Vacina {

  private UUID id;
  private String nome;
  private LocalDate dataAplicacao;

  public Vacina() {}

  public Vacina(UUID id, String nome, LocalDate dataAplicacao) {
    this.id = id;
    this.nome = nome;
    this.dataAplicacao = dataAplicacao;
  }

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
