package br.org.apae.api_crud_pacientes.application.pessoa;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public interface PessoaMapperInterface {
    //Recebe dados de entrada e transforma em uma entidade Pessoa.
    Pessoa toEntity(PessoaRequest request);

    //Recebe uma entidade Pessoa e transforma em um objeto de resposta (DTO) para retorno ao front-end, por exemplo.
    PessoaResponse toResponse(Pessoa pessoa);
}
