package br.org.apae.api_crud_pacientes.application.pessoa_responsavel;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;

import java.util.List;
import java.util.UUID;

public interface PessoaResponsavelInterface {
    PessoaResponsavel toEntity(PessoaResponsavelRequest request);
    PessoaResponsavelResponse toResponse(PessoaResponsavel request);
    // PessoaResponsavelResponse buscarPorId(UUID id);
    // PessoaResponsavelResponse buscarPorNome(String nome);
    // PessoaResponsavelResponse buscarPorCPF(String cpf);
    // List<PessoaResponsavelResponse> listarTodos();
    // PessoaResponsavelResponse atualizar(UUID id, PessoaResponsavelRequest request);
    // void deletar(UUID id);
}
