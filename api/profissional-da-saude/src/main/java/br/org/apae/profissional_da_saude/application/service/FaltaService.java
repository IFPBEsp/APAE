package br.org.apae.profissional_da_saude.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.org.apae.profissional_da_saude.api.dto.FaltaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.FaltaResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Falta;
import br.org.apae.profissional_da_saude.domain.repository.FaltaRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.FaltaMapper;

@Service
public class FaltaService {
    private final FaltaRepository repository;

    public FaltaService(FaltaRepository repository) {
        this.repository = repository;
    }

    public FaltaResponseDTO create(FaltaCreateDTO dto) {
        Falta domain = FaltaMapper.toDomain(dto);
        Falta saved = this.repository.save(domain);
        return FaltaMapper.toResponseDTO(saved);
    }

    public Page<FaltaResponseDTO> findWithFilters(UUID fkProfissional, UUID fkAtendimento, Pageable pageable) {
        return this.repository.findWithFilters(fkProfissional, fkAtendimento, pageable)
                .map(FaltaMapper::toResponseDTO);
    }

}
