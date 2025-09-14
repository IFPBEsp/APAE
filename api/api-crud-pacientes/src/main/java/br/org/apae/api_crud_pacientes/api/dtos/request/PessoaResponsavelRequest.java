package br.org.apae.api_crud_pacientes.api.dtos.request;

import java.util.UUID;

public class PessoaResponsavelRequest {
  private String nome;
  private String ondeProcurar;
  private boolean vivo;
  private String profissao;
  private String rg;
  private String cpf;
  private String emergencia;
  private String tipoResponsavel;
  private UUID pessoaId;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getOndeProcurar() {
    return ondeProcurar;
  }

  public void setOndeProcurar(String ondeProcurar) {
    this.ondeProcurar = ondeProcurar;
  }

  public UUID getPessoaId() {
    return pessoaId;
  }

  public void setPessoaId(UUID pessoaId) {
    this.pessoaId = pessoaId;
  }

  public boolean isVivo() {
    return vivo;
  }

  public void setVivo(boolean vivo) {
    this.vivo = vivo;
  }

  public String getProfissao() {
    return profissao;
  }

  public void setProfissao(String profissao) {
    this.profissao = profissao;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getEmergencia() {
    return emergencia;
  }

  public void setEmergencia(String emergencia) {
    this.emergencia = emergencia;
  }

  public String getTipoResponsavel() {
    return tipoResponsavel;
  }

  public void setTipoResponsavel(String tipoResponsavel) {
    this.tipoResponsavel = tipoResponsavel;
  }
}
