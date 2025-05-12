package br.org.apae.api_crud_pacientes.application.vacina;

import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.Vacina;

public class VacinaMapper implements VacinaMapperInterface {
    @Override
    public Vacina toEntity(VacinaRequest request, Pessoa pessoa) {
        Vacina vacina = new Vacina();
        vacina.setNome(request.getNome());
        vacina.setDataAplicacao(request.getDataAplicacao());
        vacina.setPessoa(pessoa);
        return vacina;
    }

    @Override
    public VacinaResponse toResponse(Vacina vacina) {
        VacinaResponse response = new VacinaResponse();
        response.setId(vacina.getId());
        response.setNome(vacina.getNome());
        response.setDataAplicacao(vacina.getDataAplicacao());
        return response;
    }
}
