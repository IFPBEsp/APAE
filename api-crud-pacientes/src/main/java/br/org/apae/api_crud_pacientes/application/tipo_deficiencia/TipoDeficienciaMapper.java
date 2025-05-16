package br.org.apae.api_crud_pacientes.application.tipo_deficiencia;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;
import org.springframework.stereotype.Component;

@Component
public class TipoDeficienciaMapper implements TipoDeficienciaMapperInterface {

    @Override
    public TipoDeficiencia toEntity(TipoDeficienciaRequest tipoDeficienciaDTO, Pessoa pessoa) {
        TipoDeficiencia tipoDeficiencia = new TipoDeficiencia();
        tipoDeficiencia.setDescricao(tipoDeficienciaDTO.getDescricao());
        tipoDeficiencia.setPessoa(pessoa);
        return tipoDeficiencia;
    }

    @Override
    public TipoDeficienciaResponse toResponse(TipoDeficiencia tipoDeficiencia) {
        TipoDeficienciaResponse response = new TipoDeficienciaResponse();
        response.setId(tipoDeficiencia.getId());
        response.setDescricao(tipoDeficiencia.getDescricao());
        return response;
    }

}
