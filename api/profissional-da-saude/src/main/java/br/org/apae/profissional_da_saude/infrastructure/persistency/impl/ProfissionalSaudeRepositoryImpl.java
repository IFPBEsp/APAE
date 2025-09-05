package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.domain.repository.ProfissionalSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.ProfissionalSaudeRepositoryJpa;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper;

@Repository
public class ProfissionalSaudeRepositoryImpl implements ProfissionalSaudeRepository {
  private final ProfissionalSaudeRepositoryJpa repositoryJpa;

  @Autowired
  public ProfissionalSaudeRepositoryImpl(ProfissionalSaudeRepositoryJpa repository) {
    this.repositoryJpa = repository;
  }

  @Override
  public ProfissionalSaude save(ProfissionalSaude profissionalSaude) {
    return ProfissionalSaudeMapper.toModel(repositoryJpa.save(ProfissionalSaudeMapper.toEntity(profissionalSaude)));
  }

  @Override
  public Page<ProfissionalSaude> findAll(Pageable pageable) {
    return repositoryJpa.findAll(pageable).map(ProfissionalSaudeMapper::toModel);
  }

  @Override
  public Page<String> findAllAreas(Pageable pageable) {
    return repositoryJpa.findAll(pageable).map(p -> p.getAreaDaSaude());
  }

  @Override
  public Optional<ProfissionalSaude> findById(UUID id) {
    return repositoryJpa.findById(id).map(ProfissionalSaudeMapper::toModel);
  }

  @Override
  public ProfissionalSaude update(ProfissionalSaude profissionalSaude) {
    // TODO: Implementar método
    return null;
  }

  @Override
  public void deleteById(UUID id) {
    // TODO: Implementar método
    return;
  }
}
