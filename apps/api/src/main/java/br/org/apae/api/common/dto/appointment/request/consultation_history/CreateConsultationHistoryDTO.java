package br.org.apae.api.common.dto.appointment.request.consultation_history;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateConsultationHistoryDTO(

    @NotNull(message = "Appointment ID is required") UUID appointmentId,

    @NotNull(message = "Consultation date is required") @PastOrPresent(message = "Consultation date cannot be in the future") LocalDate consultationDate,

    @NotNull(message = "Consultation time is required") LocalTime consultationTime,

    @NotNull(message = "Performed indicator is required") Boolean performed,

    String justification) {
}
