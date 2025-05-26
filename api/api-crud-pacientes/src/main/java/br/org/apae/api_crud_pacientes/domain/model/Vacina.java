package br.org.apae.api_crud_pacientes.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public class Vacina {

    private UUID id;
    private String nome;
    private LocalDate dataAplicacao;
    private Pessoa pessoa;

    public Vacina() {}

    public Vacina(UUID id, String nome, LocalDate dataAplicacao, Pessoa pessoa) {
        this.id = id;
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
        this.pessoa = pessoa;
    }

    public Vacina(String nome, LocalDate dataAplicacao, Pessoa pessoa) {
        this.nome = nome;
        this.dataAplicacao = dataAplicacao;
        this.pessoa = pessoa;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getDataAplicacao() { return dataAplicacao; }
    public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }

    public Pessoa getPessoa() { return pessoa; }
    public void setPessoa(Pessoa pessoa) { this.pessoa = pessoa; }
}