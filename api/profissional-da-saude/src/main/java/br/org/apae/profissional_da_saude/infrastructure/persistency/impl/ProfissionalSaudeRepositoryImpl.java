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

import static br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper.toEntity;
import static br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper.toModel;

@Repository
public class ProfissionalSaudeRepositoryImpl implements ProfissionalSaudeRepository {
  private final ProfissionalSaudeRepositoryJpa repositoryJpa;

  @Autowired
  public ProfissionalSaudeRepositoryImpl(ProfissionalSaudeRepositoryJpa repository) {
    this.repositoryJpa = repository;
  }

  @Override
  public ProfissionalSaude save(ProfissionalSaude profissionalSaude) {
    return toModel(
            this.repositoryJpa.save(
                    toEntity(profissionalSaude)));
  }

  @Override
  public Page<ProfissionalSaude> findAll(Pageable pageable) {
    return this.repositoryJpa.findAll(pageable)
            .map(ProfissionalSaudeMapper::toModel);
  }

  @Override
  public Optional<ProfissionalSaude> findById(UUID id) {
    return this.repositoryJpa.findById(id)
            .map(ProfissionalSaudeMapper::toModel);
  }

  @Override
  public ProfissionalSaude update(ProfissionalSaude profissionalSaude) {
    return ProfissionalSaudeMapper.toModel(
           this.repositoryJpa.save(
                    ProfissionalSaudeMapper.toEntity(profissionalSaude)
            )
    );
  }

  @Override
  public void deleteById(UUID id) {
    this.repositoryJpa.deleteById(id);
  }

  @Override
  public boolean existsByEmail(String email) {
    return this.repositoryJpa.existsByEmail(email);
  }

  @Override
  public boolean existsByDocProfissional(String docProfissional) {
    return this.repositoryJpa.existsByDocProfissional(docProfissional);
  }

}
