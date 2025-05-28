package br.org.apae.api_crud_pacientes.api.dtos.request;

import java.util.UUID;

public class ContatoRequest {
  private String enderecoAtivo;
  private String comprovanteResidencia;
  private String endereco;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;
  private String naturalidade;
  private UUID pessoaId;

  public UUID getPessoaId() {
    return pessoaId;
  }

  public void setPessoaId(UUID pessoaId) {
    this.pessoaId = pessoaId;
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
}
