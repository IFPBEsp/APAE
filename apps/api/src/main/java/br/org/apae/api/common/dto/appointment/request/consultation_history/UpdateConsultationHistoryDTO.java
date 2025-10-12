package br.org.apae.api.common.dto.appointment.request.consultation_history;

import jakarta.validation.constraints.NotNull;

public record UpdateConsultationHistoryDTO(

    @NotNull(message = "Performed indicator is required") Boolean performed,

    String justification) {
}
