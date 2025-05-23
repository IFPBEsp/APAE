package br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual;

import java.util.UUID;


public class CadastroAnualRequest {

    private Boolean beneficioDePrestacaoContinuada;

    private String historicosAlergias;
    private String medicacoesContinuas;
    private String historicoDoencas;
    private Double rendaFamiliar;
    private UUID pessoaId;
    private Long tipoAtendimentoId;

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

    public UUID getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(UUID pessoaId) {
        this.pessoaId = pessoaId;
    }

    public Long getTipoAtendimentoId() {
        return tipoAtendimentoId;
    }

    public void setTipoAtendimentoId(Long tipoAtendimentoId) {
        this.tipoAtendimentoId = tipoAtendimentoId;
    }
}
