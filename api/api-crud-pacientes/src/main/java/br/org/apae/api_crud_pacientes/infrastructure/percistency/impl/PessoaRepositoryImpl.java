package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import br.org.apae.api_crud_pacientes.domain.model.pessoa.Pessoa;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaRepository;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.pessoa.PessoaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaRepositoryJpa;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaRepositoryImpl implements PessoaRepository {

  private final PessoaRepositoryJpa repository;
  private final PessoaMapper mapper;

  @Autowired
  public PessoaRepositoryImpl(PessoaRepositoryJpa repository, PessoaMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Pessoa save(Pessoa pessoa) {
    PessoaEntity entity = mapper.toEntityFromDomain(pessoa);
    PessoaEntity saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Pessoa findById(UUID id) {
    return this.repository.findById(id).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Page<Pessoa> findAll(Pageable pageable) {
    return this.repository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public Page<Pessoa> findByNomeIgnoreCase(String nome, Pageable pageable) {
    return this.repository
        .findByNomeCompletoContainingIgnoreCase(nome, pageable)
        .map(mapper::toDomain);
  }

  @Override
  public Pessoa update(Pessoa pessoa) {
    return save(pessoa);
  }

  @Override
  public void deleteById(UUID id) {
    this.repository.deleteById(id);
  }
}
