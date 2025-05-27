package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import br.org.apae.api_crud_pacientes.domain.model.Vacina;
import br.org.apae.api_crud_pacientes.domain.repository.VacinaRepository;
import br.org.apae.api_crud_pacientes.infrastructure.entity.VacinaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.VacinaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.VacinaRepositoryJpa;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class VacinaRepositoryImpl implements VacinaRepository {

  private final VacinaRepositoryJpa repository;
  private final VacinaMapper mapper;

  @Autowired
  public VacinaRepositoryImpl(VacinaRepositoryJpa repository, VacinaMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Vacina save(Vacina vacina) {
    VacinaEntity entity = mapper.toEntityFromDomain(vacina);
    VacinaEntity saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public Vacina findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Page<Vacina> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public Vacina update(Vacina vacina) {
    return save(vacina);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }

  @Override
  public Page<Vacina> findByNomeIgnoreCase(String nome, Pageable pageable) {
    return repository.findByNomeContainingIgnoreCase(nome, pageable).map(mapper::toDomain);
  }
}
