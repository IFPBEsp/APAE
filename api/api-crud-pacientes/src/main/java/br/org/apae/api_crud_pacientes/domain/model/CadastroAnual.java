package br.org.apae.api_crud_pacientes.domain.model;

import java.util.UUID;

public class CadastroAnual {

  private UUID id;
  private Boolean beneficioDePrestacaoContinuada;
  private String historicosAlergias;
  private String medicacoesContinuas;
  private String historicoDoencas;
  private Double rendaFamiliar;
  private Pessoa pessoa;

  public CadastroAnual() {}

  public CadastroAnual(
      UUID id,
      Boolean beneficioDePrestacaoContinuada,
      String historicosAlergias,
      String medicacoesContinuas,
      String historicoDoencas,
      Double rendaFamiliar,
      Pessoa pessoa) {
    this.id = id;
    this.beneficioDePrestacaoContinuada = beneficioDePrestacaoContinuada;
    this.historicosAlergias = historicosAlergias;
    this.medicacoesContinuas = medicacoesContinuas;
    this.historicoDoencas = historicoDoencas;
    this.rendaFamiliar = rendaFamiliar;
    this.pessoa = pessoa;
  }

  public CadastroAnual(Boolean beneficioDePrestacaoContinuada, Pessoa pessoa) {
    this.beneficioDePrestacaoContinuada = beneficioDePrestacaoContinuada;
    this.pessoa = pessoa;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Boolean getBeneficioDePrestacaoContinuada() {
    return beneficioDePrestacaoContinuada;
  }

  public void setBeneficioDePrestacaoContinuada(Boolean beneficioDePrestacaoContinuada) {
    this.beneficioDePrestacaoContinuada = beneficioDePrestacaoContinuada;
  }

  public String getHistoricosAlergias() {
    return historicosAlergias;
  }

  public void setHistoricosAlergias(String historicosAlergias) {
    this.historicosAlergias = historicosAlergias;
  }

  public String getMedicacoesContinuas() {
    return medicacoesContinuas;
  }

  public void setMedicacoesContinuas(String medicacoesContinuas) {
    this.medicacoesContinuas = medicacoesContinuas;
  }

  public String getHistoricoDoencas() {
    return historicoDoencas;
  }

  public void setHistoricoDoencas(String historicoDoencas) {
    this.historicoDoencas = historicoDoencas;
  }

  public Double getRendaFamiliar() {
    return rendaFamiliar;
  }

  public void setRendaFamiliar(Double rendaFamiliar) {
    this.rendaFamiliar = rendaFamiliar;
  }

  public Pessoa getPessoa() {
    return pessoa;
  }

  public void setPessoa(Pessoa pessoa) {
    this.pessoa = pessoa;
  }
}
