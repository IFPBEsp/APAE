package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import br.org.apae.api_crud_pacientes.api.dtos.request.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.ContatoResponse;
import br.org.apae.api_crud_pacientes.domain.model.Contato;
import br.org.apae.api_crud_pacientes.infrastructure.entity.ContatoEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.GenericMapperInterface;
import org.springframework.stereotype.Component;

@Component
public class ContatoMapper
    implements GenericMapperInterface<
        ContatoRequest, ContatoResponse, ContatoEntity, PessoaEntity, Contato> {

  @Override
  public ContatoEntity toEntity(ContatoRequest request, PessoaEntity pessoa) {
    ContatoEntity contato = new ContatoEntity();

    contato.setEnderecoAtivo(request.getEnderecoAtivo());
    contato.setComprovanteResidencia(request.getComprovanteResidencia());
    contato.setEndereco(request.getEndereco());
    contato.setBairro(request.getBairro());
    contato.setCidade(request.getCidade());
    contato.setEstado(request.getEstado());
    contato.setCep(request.getCep());
    contato.setNaturalidade(request.getNaturalidade());
    contato.setTelefone(request.getTelefone());
    contato.setPessoa(pessoa);

    return contato;
  }

  @Override
  public ContatoEntity toEntityFromDomain(Contato contato) {
    ContatoEntity contatoEntity = new ContatoEntity();

    contatoEntity.setEnderecoAtivo(contato.getEnderecoAtivo());
    contatoEntity.setComprovanteResidencia(contato.getComprovanteResidencia());
    contatoEntity.setEndereco(contato.getEndereco());
    contatoEntity.setBairro(contato.getBairro());
    contatoEntity.setCidade(contato.getCidade());
    contatoEntity.setEstado(contato.getEstado());
    contatoEntity.setCep(contato.getCep());
    contatoEntity.setTelefone(contato.getTelefone());
    contatoEntity.setNaturalidade(contato.getNaturalidade());

    return contatoEntity;
  }

  @Override
  public ContatoResponse toResponse(ContatoEntity contato) {
    ContatoResponse response = new ContatoResponse();

    response.setId(contato.getId());
    response.setEnderecoAtivo(contato.getEnderecoAtivo());
    response.setComprovanteResidencia(contato.getComprovanteResidencia());
    response.setEndereco(contato.getEndereco());
    response.setBairro(contato.getBairro());
    response.setCidade(contato.getCidade());
    response.setEstado(contato.getEstado());
    response.setCep(contato.getCep());
    response.setTelefone(contato.getTelefone());
    response.setNaturalidade(contato.getNaturalidade());

    return response;
  }

  @Override
  public Contato toDomain(ContatoEntity contatoEntity) {
    Contato contato = new Contato();
    contato.setId(contatoEntity.getId());
    contato.setEnderecoAtivo(contatoEntity.getEnderecoAtivo());
    contato.setComprovanteResidencia(contatoEntity.getComprovanteResidencia());
    contato.setEndereco(contatoEntity.getEndereco());
    contato.setBairro(contatoEntity.getBairro());
    contato.setCidade(contatoEntity.getCidade());
    contato.setEstado(contatoEntity.getEstado());
    contato.setCep(contatoEntity.getCep());
    contato.setTelefone(contatoEntity.getTelefone());
    contato.setNaturalidade(contatoEntity.getNaturalidade());

    return contato;
  }

  @Override
  public ContatoResponse toResponseFromDomain(Contato contato) {
    ContatoResponse response = new ContatoResponse();
    response.setId(contato.getId());
    response.setEnderecoAtivo(contato.getEnderecoAtivo());
    response.setComprovanteResidencia(contato.getComprovanteResidencia());
    response.setEndereco(contato.getEndereco());
    response.setBairro(contato.getBairro());
    response.setCidade(contato.getCidade());
    response.setEstado(contato.getEstado());
    response.setCep(contato.getCep());
    response.setTelefone(contato.getTelefone());
    response.setNaturalidade(contato.getNaturalidade());
    return response;
  }
}
