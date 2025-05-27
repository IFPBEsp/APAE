package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import br.org.apae.api_crud_pacientes.api.dtos.request.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoDeficienciaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.GenericMapperInterface;
import org.springframework.stereotype.Component;

@Component
public class TipoDeficienciaMapper
    implements GenericMapperInterface<
        TipoDeficienciaRequest,
        TipoDeficienciaResponse,
        TipoDeficienciaEntity,
        PessoaEntity,
        TipoDeficiencia> {

  private final PessoaMapper pessoaMapper;

  public TipoDeficienciaMapper(PessoaMapper pessoaMapper) {
    this.pessoaMapper = pessoaMapper;
  }

  @Override
  public TipoDeficienciaEntity toEntity(TipoDeficienciaRequest request, PessoaEntity pessoa) {
    TipoDeficienciaEntity entity = new TipoDeficienciaEntity();
    entity.setDescricao(request.getDescricao());
    entity.setPessoa(pessoa);
    return entity;
  }

  @Override
  public TipoDeficienciaEntity toEntityFromDomain(TipoDeficiencia domain) {
    TipoDeficienciaEntity entity = new TipoDeficienciaEntity();
    entity.setId(domain.getId());
    entity.setDescricao(domain.getDescricao());
    entity.setPessoa(pessoaMapper.toEntityFromDomain(domain.getPessoa()));
    return entity;
  }

  @Override
  public TipoDeficienciaResponse toResponse(TipoDeficienciaEntity entity) {
    TipoDeficienciaResponse response = new TipoDeficienciaResponse();
    response.setId(entity.getId());
    response.setDescricao(entity.getDescricao());
    return response;
  }

  @Override
  public TipoDeficiencia toDomain(TipoDeficienciaEntity entity) {
    TipoDeficiencia domain = new TipoDeficiencia();
    domain.setId(entity.getId());
    domain.setDescricao(entity.getDescricao());
    domain.setPessoa(pessoaMapper.toDomain(entity.getPessoa()));
    return domain;
  }

  @Override
  public TipoDeficienciaResponse toResponseFromDomain(TipoDeficiencia domain) {
    TipoDeficienciaResponse response = new TipoDeficienciaResponse();
    response.setId(domain.getId());
    response.setDescricao(domain.getDescricao());
    return response;
  }
}
