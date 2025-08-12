package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.PacienteResposeDto;
import br.org.apae.profissional_da_saude.application.service.exceptions.PacienteNaoEncontradoException;
import br.org.apae.profissional_da_saude.domain.repository.PacienteRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.PacienteMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public PacienteResposeDto findById(UUID id) {
        return this.repository.findById(id)
                .map(PacienteMapper::toResponseDto).orElseThrow(PacienteNaoEncontradoException::new);
    }

    public Page<PacienteResposeDto> findAll(Pageable pageable){
        return this.repository.findAll(pageable).map(PacienteMapper::toResponseDto);
    }

}
