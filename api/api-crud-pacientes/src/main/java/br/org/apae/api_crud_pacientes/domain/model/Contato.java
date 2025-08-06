package br.org.apae.api_crud_pacientes.domain.model;

import java.util.UUID;

public class Contato {

  private UUID id;
  private String enderecoAtivo;
  private String comprovanteResidencia;
  private String endereco;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;
  private String naturalidade;
  private Pessoa pessoa;

  public Contato() {}

  public Contato(
      UUID id,
      String enderecoAtivo,
      String comprovanteResidencia,
      String endereco,
      String bairro,
      String cidade,
      String estado,
      String cep,
      String naturalidade,
      Pessoa pessoa) {
    this.id = id;
    this.enderecoAtivo = enderecoAtivo;
    this.comprovanteResidencia = comprovanteResidencia;
    this.endereco = endereco;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
    this.naturalidade = naturalidade;
    this.pessoa = pessoa;
  }

  public Contato(String endereco, String cidade, String estado, Pessoa pessoa) {
    this.endereco = endereco;
    this.cidade = cidade;
    this.estado = estado;
    this.pessoa = pessoa;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEnderecoAtivo() {
    return enderecoAtivo;
  }

  public void setEnderecoAtivo(String enderecoAtivo) {
    this.enderecoAtivo = enderecoAtivo;
  }

  public String getComprovanteResidencia() {
    return comprovanteResidencia;
  }

  public void setComprovanteResidencia(String comprovanteResidencia) {
    this.comprovanteResidencia = comprovanteResidencia;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getNaturalidade() {
    return naturalidade;
  }

  public void setNaturalidade(String naturalidade) {
    this.naturalidade = naturalidade;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }
}
