package br.org.apae.api_crud_pacientes.api.dtos.response;

import java.util.UUID;

public class ContatoResponse {
  private UUID id;
  private String enderecoAtivo;
  private String comprovanteResidencia;
  private String endereco;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;
  private String naturalidade;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    private String telefone;

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
}
