package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;

public class AgendamentoMapper {


    public static AgendamentoEntity toEntity(Agendamento agendamento) {
        return AgendamentoEntity.builder()
            .idPaciente(agendamento.getIdPaciente())
            .idProfissional(agendamento.getIdProfissional())
            .proximaConsulta(agendamento.getProximaConsulta())
            .frequenciaDias(agendamento.getFrequenciaDias())
            .build();
    }

    public static Agendamento toModel(AgendamentoEntity agendamentoEntity) {
        return new Agendamento(
            agendamentoEntity.getId(),
            agendamentoEntity.getIdPaciente(),
            agendamentoEntity.getIdProfissional(),
            agendamentoEntity.getFrequenciaDias(),
            agendamentoEntity.getProximaConsulta(),
            agendamentoEntity.getHoraProximaConsulta(),
            agendamentoEntity.getDataCriacao()
        );
    }


    public static Agendamento toDomain(AgendamentoCreateDTO dto) {
        return new Agendamento(
            dto.getIdPaciente(),
            dto.getIdProfissional(),
            dto.getFrequenciaDias(),
            dto.getProximaConsulta(),
            dto.getHoraProximaConsulta()
        );
    }

    public static AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        return AgendamentoResponseDTO.builder()
            .id(agendamento.getId())
            .idPaciente(agendamento.getIdPaciente())
            .idProfissional(agendamento.getIdProfissional())
            .frequenciaDias(agendamento.getFrequenciaDias())
            .proximaConsulta(agendamento.getProximaConsulta())
            .horaProximaConsulta(agendamento.getHoraProximaConsulta())
            .dataCriacao(agendamento.getDataCriacao())
            .build();
    }
}

