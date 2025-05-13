package br.org.apae.api_crud_pacientes.application.tipo_atendimento;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;

public class TipoAtendimentoMapper implements TipoAntendimentoMapperInterface {
    @Override
    public TipoAtendimento toEntity(TipoAtendimentoRequest tipoAtendimentoRequest) {
        TipoAtendimento tipoAtendimento = new TipoAtendimento();
        tipoAtendimento.setDescricao(tipoAtendimentoRequest.getDescricao());
        return tipoAtendimento;
    }

    @Override
    public TipoAtendimentoResponse toResponse(TipoAtendimento tipoAtendimento) {
        TipoAtendimentoResponse tipoAtendimentoResponse = new TipoAtendimentoResponse();
        tipoAtendimentoResponse.setId(tipoAtendimento.getId());
        tipoAtendimentoResponse.setDescricao(tipoAtendimento.getDescricao());
        return tipoAtendimentoResponse;
    }
}
