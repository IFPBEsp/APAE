package br.org.apae.api.common.dto.appointment.response.consultation_history;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record ConsultationHistoryResponseDTO(
    UUID id,
    UUID appointmentId,
    LocalDate consultationDate,
    LocalTime consultationTime,
    boolean performed,
    String justification,
    LocalDateTime creationDate) {
}
