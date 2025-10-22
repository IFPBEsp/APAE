package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoResponseDTO;
import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;

public class AgendamentoMapper {


    public static AgendamentoEntity toEntity(Agendamento agendamento) {
        return AgendamentoEntity.builder()
                .id(agendamento.getId())
                .idPaciente(agendamento.getIdPaciente())
                .idProfissional(agendamento.getIdProfissional())
                .idAtendimento(agendamento.getIdAtendimento())
                .idCadastroAnual(agendamento.getIdCadastroAnual())
                .frequenciaDias(agendamento.getFrequenciaDias())
                .dataInicial(agendamento.getDataInicial())
                .dataFim(agendamento.getDataFim())
                .hora(agendamento.getHora())
                .ativo(agendamento.getAtivo())
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
                agendamentoEntity.getIdProfissional(),
                agendamentoEntity.getIdAtendimento(),
                agendamentoEntity.getIdCadastroAnual(),
                agendamentoEntity.getFrequenciaDias(),
                agendamentoEntity.getDataInicial(),
                agendamentoEntity.getDataFim(),
                agendamentoEntity.getHora(),
                agendamentoEntity.getAtivo(),
                agendamentoEntity.getConfirmado(),
                agendamentoEntity.getDescricao(),
                agendamentoEntity.getJustificativa(),
                agendamentoEntity.getDataCriacao()
        );
    }


    public static Agendamento toDomain(AgendamentoCreateDTO dto) {
        return new Agendamento(
                dto.getIdPaciente(),
                dto.getIdProfissional(),
                dto.getIdAtendimento(),
                dto.getIdCadastroAnual(),
                dto.getFrequenciaDias(),
                dto.getDataInicial(),
                dto.getDataFim(),
                dto.getHora(),
                dto.getAtivo(),
                dto.getConfirmado(),
                dto.getDescricao(),
                dto.getJustificativa()
        );
    }

    public static AgendamentoResponseDTO toResponseDTO(Agendamento agendamento) {
        return AgendamentoResponseDTO.builder()
                .id(agendamento.getId())
                .idPaciente(agendamento.getIdPaciente())
                .idProfissional(agendamento.getIdProfissional())
                .idAtendimento(agendamento.getIdAtendimento())
                .idCadastroAnual(agendamento.getIdCadastroAnual())
                .frequenciaDias(agendamento.getFrequenciaDias())
                .dataInicial(agendamento.getDataInicial())
                .dataFim(agendamento.getDataFim())
                .hora(agendamento.getHora())
                .ativo(agendamento.getAtivo())
                .confirmado(agendamento.getConfirmado())
                .descricao(agendamento.getDescricao())
                .justificativa(agendamento.getJustificativa())
                .dataCriacao(agendamento.getDataCriacao())
                .build();
    }
}
