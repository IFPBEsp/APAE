package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dto.PacienteRequest;
import br.org.apae.api_crud_pacientes.api.dto.PacienteResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.repository.PacienteRepository;
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
        Optional<Pessoa> optionalPaciente = pacienteRepository.findById(id);
        if(optionalPaciente.isEmpty()){
            throw new EntityNotFoundException("Paciente não encontrado");
        }

        Pessoa pessoa = optionalPaciente.get();
        return new PacienteResponse(pessoa);
    }

    public PacienteResponse criarPaciente(PacienteRequest request) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome_completo(request.getNome_completo());
        pessoa.setCpf(request.getCpf());
        // paciente.setContatos(request.getContatos());
        pessoa.setData_nascimento(request.getData_nascimento());

        Pessoa pessoaSalvo = pacienteRepository.save(pessoa);
        return new PacienteResponse(pessoaSalvo);
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
