package br.org.apae.api_crud_pacientes.api.dtos.request;

import java.time.LocalDate;
import java.util.UUID;

public class VacinaRequest {

    private String nome;
    private LocalDate dataAplicacao;
    private UUID pessoaId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public UUID getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(UUID pessoaId) {
        this.pessoaId = pessoaId;
    }
}
