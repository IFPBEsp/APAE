package br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento;

import java.util.UUID;

public class TipoAtendimentoRequest {
    private String descricao;
    private UUID pessoaId;
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public UUID getPessoaId() {
        return pessoaId;
    }
    public void setPessoaId(UUID pessoaId) {
        this.pessoaId = pessoaId;
    }
}
