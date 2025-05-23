package br.org.apae.api_crud_pacientes.application.vacina;

import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.Vacina;

public interface VacinaMapperInterface {
    //Recebe dados de entrada e transforma em um objeto Pessoa.
    Vacina toEntity(VacinaRequest request, Pessoa pessoa);

    //Recebe um objeto Pessoa e transforma em um objeto de resposta (DTO) para retorno ao front-end, por exemplo.
    VacinaResponse toResponse(Vacina vacina);
}
