package br.org.apae.api_crud_de_grupos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "nome_grupo", nullable = false)
    private String nomeDoGrupo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ElementCollection
    @CollectionTable(name = "grupo_pacientes", joinColumns = @JoinColumn(name = "grupo_id"))
    @Column(name = "paciente_id")
    private List<UUID> listaDePacientesId = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "grupo_profissionais", joinColumns = @JoinColumn(name = "grupo_id"))
    @Column(name = "profissional_id")
    private List<UUID> listaDeProfissionaisDeSaudeId = new ArrayList<>();

    //Construtor vazio para o JPA
    public Grupo() {}

    public Grupo(String nomeDoGrupo, String descricao, List<UUID> listaDePacientesId, List<UUID> listaDeProfissionaisDeSaudeId) {
        this.nomeDoGrupo = nomeDoGrupo;
        this.descricao = descricao;
        this.listaDePacientesId = listaDePacientesId;
        this.listaDeProfissionaisDeSaudeId = listaDeProfissionaisDeSaudeId;
    }

    public UUID getId() {
        return id;
    }

    public String getNomeDoGrupo() {
        return nomeDoGrupo;
    }

    public void setNomeDoGrupo(String nomeDoGrupo) {
        this.nomeDoGrupo = nomeDoGrupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<UUID> getListaDePacientesId() {
        return listaDePacientesId;
    }

    public void setListaDePacientesId(List<UUID> listaDePacientesId) {
        this.listaDePacientesId = listaDePacientesId;
    }

    public List<UUID> getListaDeProfissionaisDeSaudeId() {
        return listaDeProfissionaisDeSaudeId;
    }

    public void setListaDeProfissionaisDeSaudeId(List<UUID> listaDeProfissionaisDeSaudeId) {
        this.listaDeProfissionaisDeSaudeId = listaDeProfissionaisDeSaudeId;
    }
}
