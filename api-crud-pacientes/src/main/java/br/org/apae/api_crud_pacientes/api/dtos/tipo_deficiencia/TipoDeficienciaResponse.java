package br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia;

import java.util.UUID;

import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public class TipoDeficienciaResponse {
    private UUID id;
    private String descricao;
    private Pessoa pessoa;
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
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
