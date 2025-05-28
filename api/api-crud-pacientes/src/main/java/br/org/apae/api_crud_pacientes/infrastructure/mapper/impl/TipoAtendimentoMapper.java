package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import br.org.apae.api_crud_pacientes.api.dtos.request.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoAtendimentoEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.GenericMapperInterface;
import org.springframework.stereotype.Component;

@Component
public class TipoAtendimentoMapper
    implements GenericMapperInterface<
        TipoAtendimentoRequest,
        TipoAtendimentoResponse,
        TipoAtendimentoEntity,
        PessoaEntity,
        TipoAtendimento> {

  private final PessoaMapper pessoaMapper;

  public TipoAtendimentoMapper(PessoaMapper pessoaMapper) {
    this.pessoaMapper = pessoaMapper;
  }

  @Override
  public TipoAtendimentoEntity toEntity(TipoAtendimentoRequest request, PessoaEntity pessoa) {
    TipoAtendimentoEntity entity = new TipoAtendimentoEntity();
    entity.setDescricao(request.getDescricao());
    entity.setPessoa(pessoa);
    return entity;
  }

  @Override
  public TipoAtendimentoEntity toEntityFromDomain(TipoAtendimento tipoAtendimento) {
    TipoAtendimentoEntity entity = new TipoAtendimentoEntity();
    entity.setId(tipoAtendimento.getId());
    entity.setDescricao(tipoAtendimento.getDescricao());
    entity.setPessoa(pessoaMapper.toEntityFromDomain(tipoAtendimento.getPessoa()));
    return entity;
  }

  @Override
  public TipoAtendimentoResponse toResponse(TipoAtendimentoEntity entity) {
    TipoAtendimentoResponse response = new TipoAtendimentoResponse();
    response.setId(entity.getId());
    response.setDescricao(entity.getDescricao());
    return response;
  }

  @Override
  public TipoAtendimento toDomain(TipoAtendimentoEntity entity) {
    TipoAtendimento tipoAtendimento = new TipoAtendimento();
    tipoAtendimento.setId(entity.getId());
    tipoAtendimento.setDescricao(entity.getDescricao());
    tipoAtendimento.setPessoa(pessoaMapper.toDomain(entity.getPessoa()));
    return tipoAtendimento;
  }

  @Override
  public TipoAtendimentoResponse toResponseFromDomain(TipoAtendimento tipoAtendimento) {
    TipoAtendimentoResponse response = new TipoAtendimentoResponse();
    response.setId(tipoAtendimento.getId());
    response.setDescricao(tipoAtendimento.getDescricao());
    return response;
  }
}
