package br.org.apae.api_crud_pacientes.application.tipo_atendimento;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;

public interface TipoAntendimentoMapperInterface {
    TipoAtendimento toEntity(TipoAtendimentoRequest tipoAtendimentoRequest);
    TipoAtendimentoResponse toResponse(TipoAtendimento tipoAtendimento);
}
