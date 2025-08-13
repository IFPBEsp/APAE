package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaUpdateDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HistoricoConsultaMapper {

    public HistoricoConsulta toDomain(HistoricoConsultaCreateDTO dto) {
        HistoricoConsulta historico = new HistoricoConsulta();
        historico.setIdAgendamento(dto.getIdAgendamento());
        historico.setDataConsulta(dto.getDataConsulta());
        historico.setFoiRealizada(dto.getFoiRealizada());
        historico.setJustificativa(dto.getJustificativa());
        historico.setDataCriacao(LocalDateTime.now());
        return historico;
    }

    public HistoricoConsultaResponseDTO toResponseDTO(HistoricoConsulta historico) {
        HistoricoConsultaResponseDTO dto = new HistoricoConsultaResponseDTO();
        dto.setId(historico.getId());
        dto.setIdAgendamento(historico.getIdAgendamento());
        dto.setDataConsulta(historico.getDataConsulta());
        dto.setFoiRealizada(historico.isFoiRealizada());
        dto.setJustificativa(historico.getJustificativa());
        dto.setDataCriacao(historico.getDataCriacao());
        return dto;
    }

    public HistoricoConsulta updateFromDTO(HistoricoConsultaUpdateDTO dto, HistoricoConsulta historico) {
        historico.setFoiRealizada(dto.getFoiRealizada());

        if (!dto.getFoiRealizada()) {
            historico.setJustificativa(dto.getJustificativa());
        } else {
            historico.setJustificativa(null);
        }

        return historico;
    }
}