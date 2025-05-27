package br.org.apae.api_crud_pacientes.domain.model;

import java.util.UUID;

public class PessoaResponsavel {

  public enum TipoResponsavel {
    MAE,
    PAI,
    RESPONSAVEL_LEGAL,
    OUTRO
  }

  private UUID id;
  private String ondeProcurar;
  private boolean vivo;
  private String profissao;
  private String rg;
  private String cpf;
  private String emergencia;
  private TipoResponsavel tipoResponsavel;
  private Pessoa pessoa;

  public PessoaResponsavel() {}

  public PessoaResponsavel(
      UUID id,
      String ondeProcurar,
      boolean vivo,
      String profissao,
      String rg,
      String cpf,
      String emergencia,
      TipoResponsavel tipoResponsavel,
      Pessoa pessoa) {
    this.id = id;
    this.ondeProcurar = ondeProcurar;
    this.vivo = vivo;
    this.profissao = profissao;
    this.rg = rg;
    this.cpf = cpf;
    this.emergencia = emergencia;
    this.tipoResponsavel = tipoResponsavel;
    this.pessoa = pessoa;
  }

  public PessoaResponsavel(
      String ondeProcurar, String cpf, TipoResponsavel tipoResponsavel, Pessoa pessoa) {
    this.ondeProcurar = ondeProcurar;
    this.cpf = cpf;
    this.tipoResponsavel = tipoResponsavel;
    this.pessoa = pessoa;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getOndeProcurar() {
    return ondeProcurar;
  }

  public void setOndeProcurar(String ondeProcurar) {
    this.ondeProcurar = ondeProcurar;
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

  public TipoResponsavel getTipoResponsavel() {
    return tipoResponsavel;
  }

  public void setTipoResponsavel(TipoResponsavel tipoResponsavel) {
    this.tipoResponsavel = tipoResponsavel;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }
}
