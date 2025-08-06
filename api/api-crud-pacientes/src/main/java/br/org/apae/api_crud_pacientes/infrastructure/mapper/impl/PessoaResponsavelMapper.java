package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaResponsavelEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.GenericMapperInterface;
import org.springframework.stereotype.Component;

@Component
public class PessoaResponsavelMapper
    implements GenericMapperInterface<
        PessoaResponsavelRequest,
        PessoaResponsavelResponse,
        PessoaResponsavelEntity,
        PessoaEntity,
        PessoaResponsavel> {

  private final PessoaMapper pessoaMapper;

  public PessoaResponsavelMapper(PessoaMapper pessoaMapper) {
    this.pessoaMapper = pessoaMapper;
  }

  @Override
  public PessoaResponsavelEntity toEntity(PessoaResponsavelRequest request, PessoaEntity pessoa) {
    PessoaResponsavelEntity entity = new PessoaResponsavelEntity();
    entity.setOndeProcurar(request.getOndeProcurar());
    entity.setVivo(request.isVivo());
    entity.setProfissao(request.getProfissao());
    entity.setRg(request.getRg());
    entity.setCpf(request.getCpf());
    entity.setEmergencia(request.getEmergencia());
    entity.setPessoa(pessoa);
    if (request.getTipoResponsavel() != null && !request.getTipoResponsavel().isEmpty()) {
      entity.setTipoResponsavel(
          PessoaResponsavelEntity.TipoResponsavel.valueOf(request.getTipoResponsavel()));
    } else {
      entity.setTipoResponsavel(null);
    }
    return entity;
  }

  @Override
  public PessoaResponsavelResponse toResponse(PessoaResponsavelEntity entity) {
    PessoaResponsavelResponse response = new PessoaResponsavelResponse();
    response.setId(entity.getId());
    response.setOndeProcurar(entity.getOndeProcurar());
    response.setVivo(entity.isVivo());
    response.setProfissao(entity.getProfissao());
    response.setRg(entity.getRg());
    response.setCpf(entity.getCpf());
    response.setEmergencia(entity.getEmergencia());
    if (entity.getTipoResponsavel() != null) {
      response.setTipoResponsavel(entity.getTipoResponsavel().name());
    }

    return response;
  }

  @Override
  public PessoaResponsavel toDomain(PessoaResponsavelEntity entity) {
    if (entity == null) return null;
    PessoaResponsavel domain = new PessoaResponsavel();
    domain.setId(entity.getId());
    domain.setOndeProcurar(entity.getOndeProcurar());
    domain.setVivo(entity.isVivo());
    domain.setProfissao(entity.getProfissao());
    domain.setRg(entity.getRg());
    domain.setCpf(entity.getCpf());
    domain.setEmergencia(entity.getEmergencia());
    if (entity.getTipoResponsavel() != null) {
      domain.setTipoResponsavel(
          PessoaResponsavel.TipoResponsavel.valueOf(entity.getTipoResponsavel().name()));
    }
    if (entity.getPessoa() != null) {
      domain.setPessoa(pessoaMapper.toDomain(entity.getPessoa()));
    }
    return domain;
  }

  @Override
  public PessoaResponsavelResponse toResponseFromDomain(PessoaResponsavel domain) {
    if (domain == null) return null;
    PessoaResponsavelResponse response = new PessoaResponsavelResponse();
    response.setId(domain.getId());
    response.setOndeProcurar(domain.getOndeProcurar());
    response.setVivo(domain.isVivo());
    response.setProfissao(domain.getProfissao());
    response.setRg(domain.getRg());
    response.setCpf(domain.getCpf());
    response.setEmergencia(domain.getEmergencia());
    response.setTipoResponsavel(domain.getTipoResponsavel().name());

    return response;
  }

  @Override
  public PessoaResponsavelEntity toEntityFromDomain(PessoaResponsavel domain) {
    if (domain == null) return null;
    PessoaResponsavelEntity entity = new PessoaResponsavelEntity();
    entity.setId(domain.getId());
    entity.setOndeProcurar(domain.getOndeProcurar());
    entity.setVivo(domain.isVivo());
    entity.setProfissao(domain.getProfissao());
    entity.setRg(domain.getRg());
    entity.setCpf(domain.getCpf());
    entity.setEmergencia(domain.getEmergencia());
    if (domain.getTipoResponsavel() != null) {
      entity.setTipoResponsavel(
          PessoaResponsavelEntity.TipoResponsavel.valueOf(domain.getTipoResponsavel().name()));
    } else {
      entity.setTipoResponsavel(null);
    }
    if (domain.getPessoa() != null) {
      entity.setPessoa(pessoaMapper.toEntityFromDomain(domain.getPessoa()));
    }
    return entity;
  }
}
