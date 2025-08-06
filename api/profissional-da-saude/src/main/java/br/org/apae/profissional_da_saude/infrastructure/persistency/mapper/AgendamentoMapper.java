package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;

import java.util.Optional;

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
            agendamentoEntity.getCreateAt()
        );
    }


    public static Agendamento toDomain(AgendamentoEntity agendamentoEntity) {
        return new Agendamento(
            agendamentoEntity.getIdPaciente(),
            agendamentoEntity.getIdProfissional(),
            agendamentoEntity.getFrequenciaDias(),
            agendamentoEntity.getProximaConsulta(),
            agendamentoEntity.getCreateAt()
        );

    }
}

