package br.org.apae.api_crud_pacientes.api.dtos.contato;

import java.util.UUID;

public class ContatoResponse {
    private UUID id;
    private String endereco_ativo;
    private String comprovante_residencia;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String naturalidade;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEndereco_ativo() {
        return endereco_ativo;
    }

    public void setEndereco_ativo(String endereco_ativo) {
        this.endereco_ativo = endereco_ativo;
    }

    public String getComprovante_residencia() {
        return comprovante_residencia;
    }

    public void setComprovante_residencia(String comprovante_residencia) {
        this.comprovante_residencia = comprovante_residencia;
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
