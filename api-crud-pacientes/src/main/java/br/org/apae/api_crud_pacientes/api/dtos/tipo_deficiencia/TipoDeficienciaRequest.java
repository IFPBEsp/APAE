package br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia;

import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public class TipoDeficienciaRequest {
    private String descricao;
    private Pessoa pessoa;
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
