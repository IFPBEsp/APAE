package br.org.apae.profissional_da_saude.domain.model;

import java.util.UUID;


public class ProfissionalSaude {
  private UUID id;
  private  String areaDaSaude;
  private  String telefone;
  private  String docProfissional;
  private  String email;
  private  String nome;

  private  String rg;
  private  String estado;
  private  String cidade;
  private  String bairro;
  private  String rua;
  private  String numero;
  private  String cep;
  private  String complemento;

  public ProfissionalSaude(UUID id, String areaDaSaude, String telefone,
                           String docProfissional, String email, String nome,
                           String rg, String estado, String cidade, String bairro,
                           String rua, String numero, String cep, String complemento) {
    this.id = id;
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
    this.rg = rg;
    this.estado = estado;
    this.cidade = cidade;
    this.bairro = bairro;
    this.rua = rua;
    this.numero = numero;
    this.cep = cep;
    this.complemento = complemento;
  }

  public ProfissionalSaude(String areaDaSaude, String telefone, String docProfissional,
                           String email, String nome, String rg,
                           String estado, String cidade, String bairro,
                           String rua, String numero, String cep, String complemento) {
    this.areaDaSaude = areaDaSaude;
    this.telefone = telefone;
    this.docProfissional = docProfissional;
    this.email = email;
    this.nome = nome;
    this.rg = rg;
    this.estado = estado;
    this.cidade = cidade;
    this.bairro = bairro;
    this.rua = rua;
    this.numero = numero;
    this.cep = cep;
    this.complemento = complemento;
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

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getRua() {
    return rua;
  }

  public void setRua(String rua) {
    this.rua = rua;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }
}
