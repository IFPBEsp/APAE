package br.org.apae.api_crud_pacientes.application.tipo_deficiencia;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;

public class TipoDeficienciaMapper implements TipoDeficienciaMapperInterface {

    @Override
    public TipoDeficiencia toEntity(TipoDeficienciaRequest tipoDeficienciaDTO) {
        TipoDeficiencia tipoDeficiencia = new TipoDeficiencia();
        tipoDeficiencia.setDescricao(tipoDeficienciaDTO.getDescricao());
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
