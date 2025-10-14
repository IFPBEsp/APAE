package br.org.apae.api.appointment.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import br.org.apae.api.appointment.domain.model.ConsultationHistory;
import br.org.apae.api.common.dto.appointment.request.consultation_history.CreateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.request.consultation_history.UpdateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.response.consultation_history.ConsultationHistoryResponseDTO;

@Component
public class ConsultationHistoryMapper {
  public ConsultationHistory toEntity(CreateConsultationHistoryDTO dto) {
    return new ConsultationHistory(
        dto.appointmentId(),
        dto.consultationDate(),
        dto.consultationTime(),
        dto.performed(),
        dto.justification(),
        LocalDateTime.now());
  }

  public ConsultationHistory updateEntity(ConsultationHistory entity, UpdateConsultationHistoryDTO dto) {
    return new ConsultationHistory(entity.getId(), entity.getAppointmentId(),
        entity.getConsultationDate(), entity.getConsultationTime(), dto.performed(), dto.justification(),
        entity.getCreationDate());
  }

  public ConsultationHistoryResponseDTO toResponse(ConsultationHistory entity) {
    return new ConsultationHistoryResponseDTO(
        entity.getId(),
        entity.getAppointmentId(),
        entity.getConsultationDate(),
        entity.getConsultationTime(),
        entity.isWasCompleted(),
        entity.getJustification(),
        entity.getCreationDate());
  }
}
