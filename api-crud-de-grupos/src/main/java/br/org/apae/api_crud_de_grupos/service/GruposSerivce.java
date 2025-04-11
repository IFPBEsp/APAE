package br.org.apae.api_crud_de_grupos.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.org.apae.api_crud_de_grupos.model.Grupo;
import br.org.apae.api_crud_de_grupos.model.Paciente;

@Service
public class GruposSerivce {
    public void inserirPaciente(Paciente p, Grupo g) {
        ArrayList<Paciente> novaLista = g.getListaDeParticipantes();
        novaLista.add(p);
        g.setListaDeParticipantes(novaLista);
    }

}
