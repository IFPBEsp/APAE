package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.AreaSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AreaSaudeResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.AreaSaudeUpdateDTO;
import br.org.apae.profissional_da_saude.domain.exception.EntidadeNaoEncontradaException;
import br.org.apae.profissional_da_saude.domain.model.AreaSaude;
import br.org.apae.profissional_da_saude.domain.repository.AreaSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AreaSaudeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AreaSaudeService {

    private final AreaSaudeRepository repository;

    public AreaSaudeService(AreaSaudeRepository repository) {
        this.repository = repository;
    }

    public AreaSaudeResponseDTO save(AreaSaudeCreateDTO dto){
        AreaSaude domain = new AreaSaude(
                dto.getArea()
        );
        AreaSaude saved = this.repository.save(domain);
        return AreaSaudeMapper.toResponseDTO(saved);
    }

    public Page<AreaSaudeResponseDTO> findAll(Pageable pageable){
        return this.repository.findAll(pageable).map(AreaSaudeMapper::toResponseDTO);
    }

    public Optional<AreaSaudeResponseDTO> findById(Integer id){
        Optional<AreaSaudeResponseDTO> dto =
                this.repository.findById(id).map(AreaSaudeMapper::toResponseDTO);

        if (dto.isEmpty()) throw new EntidadeNaoEncontradaException("Área de saúde não encontrada");

        return dto;
    }

    public void deleteById(Integer id){
        this.repository.deleteById(id);
    }

    public AreaSaudeResponseDTO update(Integer id, AreaSaudeUpdateDTO dto){
        AreaSaude existe = this.repository.findById(id)
                .orElseThrow(()-> new EntidadeNaoEncontradaException("Área de saúde não encontrada"));

        Optional.ofNullable(dto.getArea()).ifPresent(existe::setArea);

        AreaSaude saved = this.repository.save(existe);

        return  AreaSaudeMapper.toResponseDTO(saved);
    }
}
