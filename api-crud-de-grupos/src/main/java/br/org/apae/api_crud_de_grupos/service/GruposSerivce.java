package br.org.apae.api_crud_de_grupos.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.apae.api_crud_de_grupos.model.Grupo;
import br.org.apae.api_crud_de_grupos.model.Paciente;

@Service
public class GruposSerivce {
    public void inserirPaciente(UUID pacienteId, Grupo grupo) {
        grupo.getListaDePacientesId().add(pacienteId);
    }

}
