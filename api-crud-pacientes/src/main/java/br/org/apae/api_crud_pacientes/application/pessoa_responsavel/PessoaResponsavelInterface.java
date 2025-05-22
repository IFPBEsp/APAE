package br.org.apae.api_crud_pacientes.application.pessoa_responsavel;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;

import java.util.List;
import java.util.UUID;

public interface PessoaResponsavelInterface {
    PessoaResponsavel toEntity(PessoaResponsavelRequest request, Pessoa pessoa);
    PessoaResponsavelResponse toResponse(PessoaResponsavel request);
}
