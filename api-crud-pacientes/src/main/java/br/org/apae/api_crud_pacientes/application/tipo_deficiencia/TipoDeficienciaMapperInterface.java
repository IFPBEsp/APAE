package br.org.apae.api_crud_pacientes.application.tipo_deficiencia;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;

public interface TipoDeficienciaMapperInterface {
    TipoDeficiencia toEntity(TipoDeficienciaRequest tipoDeficienciaDTO);
    TipoDeficienciaResponse toResponse(TipoDeficiencia tipoDeficiencia);
}
