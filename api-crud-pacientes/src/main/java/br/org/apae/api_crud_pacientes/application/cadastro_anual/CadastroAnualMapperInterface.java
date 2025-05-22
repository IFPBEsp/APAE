package br.org.apae.api_crud_pacientes.application.cadastro_anual;

import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public interface CadastroAnualMapperInterface {
    CadastroAnual toEntity(CadastroAnualRequest request, Pessoa pessoa);
    CadastroAnualResponse toResponse(CadastroAnual cadastro);

}
