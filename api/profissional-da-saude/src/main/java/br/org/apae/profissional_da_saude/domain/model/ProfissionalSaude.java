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
  private  String rg;

  private Endereco endereco;


  public ProfissionalSaude(UUID id, String areaDaSaude, String telefone,
                           String docProfissional, String email, String nome,
                           String rg, Endereco endereco) {
    this.id = id;
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
    this.rg = rg;
    this.endereco = endereco;
  }

  public ProfissionalSaude(String areaDaSaude, String telefone, String docProfissional,
                           String email, String nome, String rg, Endereco endereco) {
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
    this.rg = rg;
    this.endereco = endereco;

  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
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
  public String getAreaDaSaude() {
    return areaDaSaude;
  }

  public void setAreaDaSaude(String areaDaSaude) {
    this.areaDaSaude = areaDaSaude;
  }

  public String getDocProfissional() {
    return docProfissional;
  }

  public void setDocProfissional(String docProfissional) {
    this.docProfissional = docProfissional;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public void setDisponibilidades(List<DisponibilidadeEntity> disponibilidades) {
      this.disponibilidades = disponibilidades;
  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }
}
