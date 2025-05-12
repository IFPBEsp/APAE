package br.org.apae.api_crud_pacientes.application.contato;

import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoResponse;
import br.org.apae.api_crud_pacientes.domain.model.Contato;

public interface ContatoMapperInterface {
    Contato toEntity(ContatoRequest request);

    ContatoResponse toResponse(Contato contato);
}
