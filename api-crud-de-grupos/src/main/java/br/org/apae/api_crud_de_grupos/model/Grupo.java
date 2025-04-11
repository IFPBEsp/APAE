package br.org.apae.api_crud_de_grupos.model;

import java.util.ArrayList;
import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class Grupo {
    private UUID id;
    private String nomeDoGrupo;
    private ArrayList<Paciente> listaDeParticipantes;

    public Grupo(UUID id, String nomeDoGrupo, ArrayList<Paciente> listaDeParticipantes) {
        this.id = id;
        this.nomeDoGrupo = nomeDoGrupo;
        this.listaDeParticipantes = listaDeParticipantes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeDoGrupo() {
        return nomeDoGrupo;
    }

    public void setNomeDoGrupo(String nomeDoGrupo) {
        this.nomeDoGrupo = nomeDoGrupo;
    }

    public ArrayList<Paciente> getListaDeParticipantes() {
        return listaDeParticipantes;
    }

    public void setListaDeParticipantes(ArrayList<Paciente> listaDeParticipantes) {
        this.listaDeParticipantes = listaDeParticipantes;
    }

}
