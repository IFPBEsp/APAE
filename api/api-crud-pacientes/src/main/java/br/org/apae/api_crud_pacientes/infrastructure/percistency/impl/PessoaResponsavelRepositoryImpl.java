package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaResponsavelRepository;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaResponsavelEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.PessoaResponsavelMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaResponsavelRepositoryJpa;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaResponsavelRepositoryImpl implements PessoaResponsavelRepository {

  private final PessoaResponsavelRepositoryJpa repository;
  private final PessoaResponsavelMapper mapper;

  @Autowired
  public PessoaResponsavelRepositoryImpl(
      PessoaResponsavelRepositoryJpa repository, PessoaResponsavelMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public PessoaResponsavel save(PessoaResponsavel pessoaResponsavel) {
    PessoaResponsavelEntity entity = mapper.toEntityFromDomain(pessoaResponsavel);
    PessoaResponsavelEntity saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public PessoaResponsavel findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Page<PessoaResponsavel> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public PessoaResponsavel update(PessoaResponsavel pessoaResponsavel) {
    return save(pessoaResponsavel);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }

  @Override
  public Page<PessoaResponsavel> findByCpf(String cpf, Pageable pageable) {
    return repository.findByCpfContaining(cpf, pageable).map(mapper::toDomain);
  }
}
