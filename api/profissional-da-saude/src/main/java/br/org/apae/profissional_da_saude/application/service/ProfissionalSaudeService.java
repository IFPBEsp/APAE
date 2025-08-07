package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.domain.exception.EntidadeNaoEncontradaException;
import br.org.apae.profissional_da_saude.domain.exception.ValidacaoNegocioException;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.domain.repository.ProfissionalSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfissionalSaudeService {

  private final ProfissionalSaudeRepository repository;

  @Autowired
  public ProfissionalSaudeService(ProfissionalSaudeRepository repository) {
    this.repository = repository;
  }

  public ProfissionalSaudeResponseDTO save(ProfissionalSaudeCreateDTO dto) {
    if (repository.existsByDocProfissional(dto.getDocProfissional())) {
      throw new ValidacaoNegocioException("Documento profissional já cadastrado.");
    }

    if (repository.existsByEmail(dto.getEmail())) {
      throw new ValidacaoNegocioException("E-mail já cadastrado.");
    }
    ProfissionalSaude domain = ProfissionalSaudeMapper.toDomain(dto);
    ProfissionalSaude saved = repository.save(domain);
    return ProfissionalSaudeMapper.toResponseDTO(saved);
  }

  public Page<ProfissionalSaudeResponseDTO> findAll(Pageable pageable) {
    return repository.findAll(pageable)
        .map(ProfissionalSaudeMapper::toResponseDTO);
  }

  public void delete(UUID id){
    repository.findById(id).orElseThrow(
            () -> new EntidadeNaoEncontradaException("Profissional não encontrado"));

    repository.deleteById(id);
  }

  public Optional<ProfissionalSaudeResponseDTO> findById(UUID id){
    return repository.findById(id).map(ProfissionalSaudeMapper::toResponseDTO);
  }

}
