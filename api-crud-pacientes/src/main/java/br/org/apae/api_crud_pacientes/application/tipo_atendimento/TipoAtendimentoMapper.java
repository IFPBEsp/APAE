package br.org.apae.api_crud_pacientes.application.tipo_atendimento;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;
import org.springframework.stereotype.Component;

@Component
public class TipoAtendimentoMapper implements TipoAntendimentoMapperInterface {
    @Override
    public TipoAtendimento toEntity(TipoAtendimentoRequest tipoAtendimentoRequest, Pessoa pessoa) {
        TipoAtendimento tipoAtendimento = new TipoAtendimento();
        tipoAtendimento.setDescricao(tipoAtendimentoRequest.getDescricao());
        tipoAtendimento.setPessoa(pessoa);
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
