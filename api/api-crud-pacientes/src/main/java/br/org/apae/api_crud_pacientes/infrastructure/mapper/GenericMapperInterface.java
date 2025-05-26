package br.org.apae.api_crud_pacientes.infrastructure.mapper;

public interface GenericMapperInterface <Request, Response, Entity, PessoaEntity, Domain> {
    Entity toEntity(Request request, PessoaEntity pessoaEntity);
    Response toResponse(Entity entity);
    Domain toDomain(Entity entity);
    Response toResponseFromDomain(Domain domain);
    Entity toEntityFromDomain(Domain domain);
}
