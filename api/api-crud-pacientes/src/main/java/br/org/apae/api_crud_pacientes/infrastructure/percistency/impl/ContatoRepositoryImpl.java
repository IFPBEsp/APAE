package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import br.org.apae.api_crud_pacientes.domain.model.Contato;
import br.org.apae.api_crud_pacientes.domain.repository.ContatoRepository;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.ContatoMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.ContatoRepositoryJpa;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ContatoRepositoryImpl implements ContatoRepository {

  private final ContatoRepositoryJpa repository;
  private final ContatoMapper mapper;

  @Autowired
  public ContatoRepositoryImpl(ContatoRepositoryJpa repository, ContatoMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Contato save(Contato contato) {
    var entity = mapper.toEntityFromDomain(contato);
    var saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Contato findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Page<Contato> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public Page<Contato> findByEnderecoIgnoreCase(String endereco, Pageable pageable) {
    return repository.findByEnderecoIgnoreCase(endereco, pageable).map(mapper::toDomain);
  }

  @Override
  public Contato update(Contato contato) {
    return save(contato);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }
}
