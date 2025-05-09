package br.org.apae.api_crud_pacientes.domain.model;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cadastro_anual")
public class CadastroAnual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Boolean beneficioDePrestacaoContinuada;

    private String historicosAlergias;

    private String medicacoesContinuas;

    private String historicoDoencas;

    private Double rendaFamiliar;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "tipo_atendimento_id")
    private TipoAtendimento tipoAtendimento;


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

    public TipoAtendimento getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
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
