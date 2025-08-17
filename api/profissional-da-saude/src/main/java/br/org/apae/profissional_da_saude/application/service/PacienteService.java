package br.org.apae.profissional_da_saude.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.org.apae.profissional_da_saude.api.dto.PacienteCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.PacienteResposeDTO;
import br.org.apae.profissional_da_saude.application.service.exceptions.PacienteNaoEncontradoException;
import br.org.apae.profissional_da_saude.domain.model.Paciente;
import br.org.apae.profissional_da_saude.domain.repository.PacienteRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.PacienteMapper;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public PacienteResposeDTO findById(UUID id) {
        return this.repository.findById(id)
                .map(PacienteMapper::toResponseDTO).orElseThrow(PacienteNaoEncontradoException::new);
    }

    public Page<PacienteResposeDTO> findAll(Pageable pageable){
        return this.repository.findAll(pageable).map(PacienteMapper::toResponseDTO);
    }

    public PacienteResposeDTO create(PacienteCreateDTO dto) {
        Paciente domain = PacienteMapper.toDomain(dto);
        Paciente saved = this.repository.save(domain);
        return PacienteMapper.toResponseDTO(saved);
    }
}
