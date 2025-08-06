package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;
import br.org.apae.api_crud_pacientes.domain.repository.TipoDeficienciaRepository;
import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoDeficienciaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.TipoDeficienciaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.TipoDeficienciaRepositoryJpa;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TipoDeficienciaRepositoryImpl implements TipoDeficienciaRepository {

  private final TipoDeficienciaRepositoryJpa repository;
  private final TipoDeficienciaMapper mapper;

  @Autowired
  public TipoDeficienciaRepositoryImpl(
      TipoDeficienciaRepositoryJpa repository, TipoDeficienciaMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public TipoDeficiencia save(TipoDeficiencia tipoDeficiencia) {
    TipoDeficienciaEntity entity = mapper.toEntityFromDomain(tipoDeficiencia);
    TipoDeficienciaEntity saved = repository.save(entity);
    return mapper.toDomain(saved);
  }

  @Override
  public TipoDeficiencia findById(UUID id) {
    return repository.findById(id).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Page<TipoDeficiencia> findAll(Pageable pageable) {
    Page<TipoDeficienciaEntity> page = repository.findAll(pageable);
    List<TipoDeficiencia> content =
        page.getContent().stream().map(mapper::toDomain).collect(Collectors.toList());
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  public Page<TipoDeficiencia> findByDescricaoIgnoreCase(String descricao, Pageable pageable) {
    Page<TipoDeficienciaEntity> page =
        repository.findByDescricaoContainingIgnoreCase(descricao, pageable);
    List<TipoDeficiencia> content =
        page.getContent().stream().map(mapper::toDomain).collect(Collectors.toList());
    return new PageImpl<>(content, pageable, page.getTotalElements());
  }

  @Override
  public TipoDeficiencia update(TipoDeficiencia tipoDeficiencia) {
    TipoDeficienciaEntity entity = mapper.toEntityFromDomain(tipoDeficiencia);
    TipoDeficienciaEntity updated = repository.save(entity);
    return mapper.toDomain(updated);
  }

  @Override
  public void deleteById(UUID id) {
    repository.deleteById(id);
  }
}
