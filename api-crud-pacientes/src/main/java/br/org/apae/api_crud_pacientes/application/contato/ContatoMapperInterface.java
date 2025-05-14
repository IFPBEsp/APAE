package br.org.apae.api_crud_pacientes.application.contato;

import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoResponse;
import br.org.apae.api_crud_pacientes.domain.model.Contato;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public interface ContatoMapperInterface {
    Contato toEntity(ContatoRequest request, Pessoa pessoa);

    ContatoResponse toResponse(Contato contato);
}
