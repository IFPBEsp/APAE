package br.org.apae.api_crud_pacientes.api.dtos.response;

import java.util.UUID;

import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;

public class TipoDeficienciaResponse {
    private UUID id;
    private String descricao;
    private PessoaEntity pessoa;
    public PessoaEntity getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaEntity pessoa) {
        this.pessoa = pessoa;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
