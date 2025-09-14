package br.org.apae.profissional_da_saude.domain.model;

import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfissionalSaude {
  private UUID id;
  private  String areaDaSaude;
  private  String telefone;
  private  String docProfissional;
  private  String email;
  private  String nome;
  private List<DisponibilidadeEntity> disponibilidades = new ArrayList<>();

  public ProfissionalSaude(UUID id, String areaDaSaude, String telefone, String docProfissional, String email,
      String nome) {
    this.id = id;
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
  }

  public ProfissionalSaude(String areaDaSaude, String telefone, String docProfissional, String email,
      String nome) {
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
  }

  public UUID getId() {
    return id;
  }

  public String getAreaDaSaude() {
    return areaDaSaude;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getDocProfissional() {
    return docProfissional;
  }

  public String getEmail() {
    return email;
  }

  public String getNome() {
    return nome;
  }

  public List<DisponibilidadeEntity> getDisponibilidades() {
      return disponibilidades;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setAreaDaSaude(String areaDaSaude) {
    this.areaDaSaude = areaDaSaude;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public void setDocProfissional(String docProfissional) {
    this.docProfissional = docProfissional;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setDisponibilidades(List<DisponibilidadeEntity> disponibilidades) {
      this.disponibilidades = disponibilidades;
  }
}
