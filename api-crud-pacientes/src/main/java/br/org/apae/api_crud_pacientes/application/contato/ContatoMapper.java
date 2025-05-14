package br.org.apae.api_crud_pacientes.application.contato;

import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoResponse;
import br.org.apae.api_crud_pacientes.domain.model.Contato;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public class ContatoMapper implements ContatoMapperInterface {

    @Override
    public Contato toEntity(ContatoRequest request, Pessoa pessoa) {
        Contato contato = new Contato();

        contato.setEndereco_ativo(request.getEndereco_ativo());
        contato.setComprovante_residencia(request.getComprovante_residencia());
        contato.setEndereco(request.getEndereco());
        contato.setBairro(request.getBairro());
        contato.setCidade(request.getCidade());
        contato.setEstado(request.getEstado());
        contato.setCep(request.getCep());
        contato.setNaturalidade(request.getNaturalidade());

        contato.setPessoa(pessoa);

        return contato;
    }

    @Override
    public ContatoResponse toResponse(Contato contato) {
        ContatoResponse response = new ContatoResponse();

        response.setId(contato.getId());
        response.setEndereco_ativo(contato.getEndereco_ativo());
        response.setComprovante_residencia(contato.getComprovante_residencia());
        response.setEndereco(contato.getEndereco());
        response.setBairro(contato.getBairro());
        response.setCidade(contato.getCidade());
        response.setEstado(contato.getEstado());
        response.setCep(contato.getCep());
        response.setNaturalidade(contato.getNaturalidade());

        return response;
    }

}
