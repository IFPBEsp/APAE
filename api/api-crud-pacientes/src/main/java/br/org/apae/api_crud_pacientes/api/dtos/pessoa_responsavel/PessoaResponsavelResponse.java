package br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel;

import java.util.UUID;

public class PessoaResponsavelResponse {
    private UUID id;
    private String ondeProcurar;
    private boolean vivo;
    private String profissao;
    private String rg;
    private String cpf;
    private String emergencia;
    private String tipoResponsavel;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOnde_Procurar() {
        return ondeProcurar;
    }

    public void setOnde_Procurar(String ondeProcurar) {
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

    public String getTipoResponsavel() {
        return tipoResponsavel;
    }

    public void setTipoResponsavel(String tipoResponsavel) {
        this.tipoResponsavel = tipoResponsavel;
    }
}
