package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import br.org.apae.api_crud_pacientes.domain.repository.CadastroAnualRepository;
import br.org.apae.api_crud_pacientes.infrastructure.entity.CadastroAnualEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.CadastroAnualMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.CadastroAnualRepositoryJpa;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CadastroAnualRepositoryImpl implements CadastroAnualRepository {

  private final CadastroAnualRepositoryJpa repository;
  private final CadastroAnualMapper mapper;

  @Autowired
  public CadastroAnualRepositoryImpl(
      CadastroAnualRepositoryJpa repository, CadastroAnualMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public CadastroAnual save(CadastroAnual cadastroAnual) {
    CadastroAnualEntity entity = mapper.toEntityFromDomain(cadastroAnual);
    CadastroAnualEntity saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public CadastroAnual findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Page<CadastroAnual> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDomain);
  }

  @Override
  public CadastroAnual update(CadastroAnual cadastroAnual) {
    return save(cadastroAnual);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }
}
