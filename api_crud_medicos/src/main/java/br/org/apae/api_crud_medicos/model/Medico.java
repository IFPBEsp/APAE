package br.org.apae.api_crud_medicos.model;


import java.util.List;

public class Medico {

    private Long id;
    private String nome;
    private String especialidade;
    private String contato;
    private String crm;

    private List<Long>  loudIds;

    public Medico() {

    }

    public Medico(Long id, String nome, String especialidade, String contato, String crm, List<Long> loudIds) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.contato = contato;
        this.crm = crm;
        this.loudIds = loudIds;
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public List<Long> getLoudIds() {
        return loudIds;
    }

    public void setLoudIds(List<Long> loudIds) {
        this.loudIds = loudIds;
    }
}
