package br.org.apae.api_crud_pacientes.service;

import br.org.apae.api_crud_pacientes.DTO.request.PacienteRequest;
import br.org.apae.api_crud_pacientes.DTO.response.PacienteResponse;
import br.org.apae.api_crud_pacientes.model.Paciente;
import br.org.apae.api_crud_pacientes.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public PacienteResponse criarPaciente(PacienteRequest request) {
        Paciente paciente = new Paciente();
        paciente.setNome_completo(request.getNome_completo());
        paciente.setCpf(request.getCpf());
        paciente.setContatos(request.getContatos());
        paciente.setData_nascimento(request.getData_nascimento());

        Paciente pacienteSalvo = pacienteRepository.save(paciente);
        return new PacienteResponse(pacienteSalvo);
    }

    public Page<PacienteResponse> listarPacientes(Pageable pageable, String cpf, String nome) {
        if (cpf != null && nome != null) {
            return pacienteRepository.findByCpfContainingAndNomeCompletoContainingIgnoreCase(cpf, nome, pageable)
                    .map(PacienteResponse::new);
        } else if (cpf != null) {
            return pacienteRepository.findByCpfContaining(cpf, pageable)
                    .map(PacienteResponse::new);
        } else if (nome != null) {
            return pacienteRepository.findByNomeCompletoContainingIgnoreCase(nome, pageable)
                    .map(PacienteResponse::new);
        } else {
            return pacienteRepository.findAll(pageable)
                    .map(PacienteResponse::new);
        }
    }


    public void deletarPorId(UUID id) {
        if(!pacienteRepository.existsById(id)){
            throw new EntityNotFoundException("Paciente não encontrado.");
        }
        pacienteRepository.deleteById(id);
    }
}
