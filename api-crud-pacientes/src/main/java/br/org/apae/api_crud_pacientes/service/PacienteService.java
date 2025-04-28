package br.org.apae.api_crud_pacientes.service;

import br.org.apae.api_crud_pacientes.DTO.PacienteResponse;
import br.org.apae.api_crud_pacientes.model.Paciente;
import br.org.apae.api_crud_pacientes.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PacienteService {
    private PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public PacienteResponse buscarPorId(UUID id) {
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        if(optionalPaciente.isEmpty()){
            throw new EntityNotFoundException("Paciente não encontrado");
        }

        Paciente paciente = optionalPaciente.get();
        return new PacienteResponse(paciente);
    }

    public void deletarPorId(UUID id) {
        if(!pacienteRepository.existsById(id)){
            throw new EntityNotFoundException("Paciente não encontrado.");
        }
        pacienteRepository.deleteById(id);
    }
}
