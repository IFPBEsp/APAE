package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;

public class AgendamentoMapper {

    private AgendamentoMapper() {
    }

    public static AgendamentoEntity toEntity(Agendamento agendamento) {
        return AgendamentoEntity.builder()
            .id(agendamento.getId())
            .idPaciente(agendamento.getIdPaciente())
            .idAreaDaSaude(agendamento.getIdAreaDaSaude())
            .proximaConsulta(agendamento.getProximaConsulta())
            .horaProximaConsulta(agendamento.getHoraProximaConsulta())
            .frequenciaDias(agendamento.getFrequenciaDias())
            .confirmado(agendamento.getConfirmado())
            .descricao(agendamento.getDescricao())
            .justificativa(agendamento.getJustificativa())
            .dataCriacao(agendamento.getDataCriacao())
            .build();
    }

    public static Agendamento toModel(AgendamentoEntity agendamentoEntity) {
        return new Agendamento(
            agendamentoEntity.getId(),
            agendamentoEntity.getIdPaciente(),
            agendamentoEntity.getIdAreaDaSaude(),
            agendamentoEntity.getFrequenciaDias(),
            agendamentoEntity.getProximaConsulta(),
            agendamentoEntity.getHoraProximaConsulta(),
            agendamentoEntity.getConfirmado(),
            agendamentoEntity.getDescricao(),
            agendamentoEntity.getJustificativa(),
            agendamentoEntity.getDataCriacao()
        );
    }


    public static Agendamento toDomain(AgendamentoCreateDTO dto) {
        return new Agendamento(
            dto.getIdPaciente(),
            dto.getIdAreaDaSaude(),
            dto.getFrequenciaDias(),
            dto.getProximaConsulta(),
            dto.getHoraProximaConsulta(),
            dto.getConfirmado(),
            dto.getDescricao(),
            ""
        );
    }

    public static AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        return AgendamentoResponseDTO.builder()
            .id(agendamento.getId())
            .idPaciente(agendamento.getIdPaciente())
            .idAreaDaSaude(agendamento.getIdAreaDaSaude())
            .frequenciaDias(agendamento.getFrequenciaDias())
            .proximaConsulta(agendamento.getProximaConsulta())
            .horaProximaConsulta(agendamento.getHoraProximaConsulta())
            .confirmado(agendamento.getConfirmado())
            .descricao(agendamento.getDescricao())
            .justificativa(agendamento.getJustificativa())
            .dataCriacao(agendamento.getDataCriacao())
            .build();
    }
}

