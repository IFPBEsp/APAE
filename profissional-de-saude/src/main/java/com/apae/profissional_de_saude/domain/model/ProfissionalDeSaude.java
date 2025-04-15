package com.apae.profissional_de_saude.domain.model;

import java.util.List;

public class ProfissionalDeSaude {
    private Long id;
    private String nome;
    private String especialidade;
    private String contato;

    private List<Long> relatorioIds;
    private List<Long> grupoIds;

    public ProfissionalDeSaude(String contato, String especialidade, List<Long> grupoIds, Long id, String nome, List<Long> relatorioIds) {
        this.contato = contato;
        this.especialidade = especialidade;
        this.grupoIds = grupoIds;
        this.id = id;
        this.nome = nome;
        this.relatorioIds = relatorioIds;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<Long> getGrupoIds() {
        return grupoIds;
    }

    public void setGrupoIds(List<Long> grupoIds) {
        this.grupoIds = grupoIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getRelatorioIds() {
        return relatorioIds;
    }

    public void setRelatorioIds(List<Long> relatorioIds) {
        this.relatorioIds = relatorioIds;
    }
}
