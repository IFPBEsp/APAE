package br.org.apae.api_crud_pacientes.domain.model;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cadastro_anual")
public class CadastroAnual {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "beneficio_de_prestacao_continuada")
    private Boolean beneficioDePrestacaoContinuada;

    @Column(name = "historicos_alergias")
    private String historicosAlergias;

    @Column(name = "medicacoes_continuas")
    private String medicacoesContinuas;

    @Column(name = "historico_doencas")
    private String historicoDoencas;

    @Column(name = "renda_familiar")
    private Double rendaFamiliar;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CadastroAnual)) return false;
        CadastroAnual that = (CadastroAnual) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
