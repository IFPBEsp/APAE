package br.org.apae.api_crud_pacientes.application.pessoa_responsavel;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa_Responsavel;

import java.util.List;
import java.util.UUID;

public interface PessoaResponsavelInterface {
    Pessoa_Responsavel toEntity(PessoaResponsavelRequest request);
    PessoaResponsavelResponse toResponse(Pessoa_Responsavel request);
    // PessoaResponsavelResponse buscarPorId(UUID id);
    // PessoaResponsavelResponse buscarPorNome(String nome);
    // PessoaResponsavelResponse buscarPorCPF(String cpf);
    // List<PessoaResponsavelResponse> listarTodos();
    // PessoaResponsavelResponse atualizar(UUID id, PessoaResponsavelRequest request);
    // void deletar(UUID id);
}
