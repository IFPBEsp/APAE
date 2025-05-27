package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import br.org.apae.api_crud_pacientes.api.dtos.request.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.VacinaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Vacina;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.VacinaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.GenericMapperInterface;
import org.springframework.stereotype.Component;

@Component
public class VacinaMapper
    implements GenericMapperInterface<
        VacinaRequest, VacinaResponse, VacinaEntity, PessoaEntity, Vacina> {

  private final PessoaMapper pessoaMapper;

  public VacinaMapper(PessoaMapper pessoaMapper) {
    this.pessoaMapper = pessoaMapper;
  }

  @Override
  public VacinaEntity toEntity(VacinaRequest request, PessoaEntity pessoa) {
    VacinaEntity vacina = new VacinaEntity();
    vacina.setNome(request.getNome());
    vacina.setDataAplicacao(request.getDataAplicacao());
    vacina.setPessoa(pessoa);
    return vacina;
  }

  @Override
  public VacinaEntity toEntityFromDomain(Vacina domain) {
    VacinaEntity entity = new VacinaEntity();
    entity.setId(domain.getId());
    entity.setNome(domain.getNome());
    entity.setDataAplicacao(domain.getDataAplicacao());
    entity.setPessoa(pessoaMapper.toEntityFromDomain(domain.getPessoa()));
    return entity;
  }

  @Override
  public VacinaResponse toResponse(VacinaEntity entity) {
    VacinaResponse response = new VacinaResponse();
    response.setId(entity.getId());
    response.setNome(entity.getNome());
    response.setDataAplicacao(entity.getDataAplicacao());
    return response;
  }

  @Override
  public Vacina toDomain(VacinaEntity entity) {
    Vacina vacina = new Vacina();
    vacina.setId(entity.getId());
    vacina.setNome(entity.getNome());
    vacina.setDataAplicacao(entity.getDataAplicacao());
    vacina.setPessoa(pessoaMapper.toDomain(entity.getPessoa()));
    return vacina;
  }

  @Override
  public VacinaResponse toResponseFromDomain(Vacina domain) {
    VacinaResponse response = new VacinaResponse();
    response.setId(domain.getId());
    response.setNome(domain.getNome());
    response.setDataAplicacao(domain.getDataAplicacao());
    return response;
  }
}
