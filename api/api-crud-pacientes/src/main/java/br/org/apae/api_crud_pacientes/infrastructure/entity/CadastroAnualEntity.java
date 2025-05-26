package br.org.apae.api_crud_pacientes.infrastructure.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cadastro_anual")
public class CadastroAnualEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private PessoaEntity pessoa;

    public CadastroAnualEntity() {}

    public CadastroAnualEntity(UUID id, Boolean beneficioDePrestacaoContinuada, String historicosAlergias, String medicacoesContinuas, String historicoDoencas, Double rendaFamiliar, PessoaEntity pessoa) {
        this.id = id;
        this.beneficioDePrestacaoContinuada = beneficioDePrestacaoContinuada;
        this.historicosAlergias = historicosAlergias;
        this.medicacoesContinuas = medicacoesContinuas;
        this.historicoDoencas = historicoDoencas;
        this.rendaFamiliar = rendaFamiliar;
        this.pessoa = pessoa;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Boolean getBeneficioDePrestacaoContinuada() { return beneficioDePrestacaoContinuada; }
    public void setBeneficioDePrestacaoContinuada(Boolean beneficioDePrestacaoContinuada) { this.beneficioDePrestacaoContinuada = beneficioDePrestacaoContinuada; }

    public String getHistoricosAlergias() { return historicosAlergias; }
    public void setHistoricosAlergias(String historicosAlergias) { this.historicosAlergias = historicosAlergias; }

    public String getMedicacoesContinuas() { return medicacoesContinuas; }
    public void setMedicacoesContinuas(String medicacoesContinuas) { this.medicacoesContinuas = medicacoesContinuas; }

    public String getHistoricoDoencas() { return historicoDoencas; }
    public void setHistoricoDoencas(String historicoDoencas) { this.historicoDoencas = historicoDoencas; }

    public Double getRendaFamiliar() { return rendaFamiliar; }
    public void setRendaFamiliar(Double rendaFamiliar) { this.rendaFamiliar = rendaFamiliar; }

    public PessoaEntity getPessoa() { return pessoa; }
    public void setPessoa(PessoaEntity pessoa) { this.pessoa = pessoa; }
}
