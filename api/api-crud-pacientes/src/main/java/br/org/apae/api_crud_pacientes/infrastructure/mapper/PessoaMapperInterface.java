package br.org.apae.api_crud_pacientes.infrastructure.mapper;

public interface PessoaMapperInterface<Request, Response, Entity, Domain> {
  Entity toEntity(Request request);

  Response toResponse(Entity entity);

  Domain toDomain(Entity entity);

  Entity toEntityFromDomain(Domain domain);
}
