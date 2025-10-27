package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoGeradoResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.FaltaDTO;
import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;

public class AgendamentoGeradoMapper {

    public static AgendamentoGeradoResponseDTO toResponseDTO(AgendamentoGerado agendamentoGerado) {
        List<FaltaDTO> faltasDTO = agendamentoGerado.getFaltas() != null ?
            agendamentoGerado.getFaltas().stream()
                .map(falta -> new FaltaDTO(falta.getId(), falta.getMotivo()))
                .collect(Collectors.toList()) : null;
        return new AgendamentoGeradoResponseDTO(
            agendamentoGerado.getId(), 
            agendamentoGerado.getFrequenciaDias(), 
            agendamentoGerado.getDataInicial(),
            agendamentoGerado.getHora(),
            agendamentoGerado.getDataFim(),
            agendamentoGerado.getAtivo(),
            agendamentoGerado.getFkAtendimento(),
            agendamentoGerado.getFkProfissional(),
            agendamentoGerado.getFkCadastroAnual(),
            faltasDTO
        );
    }
}