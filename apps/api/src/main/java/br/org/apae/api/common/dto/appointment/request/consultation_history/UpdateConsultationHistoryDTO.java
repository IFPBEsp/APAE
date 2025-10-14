package br.org.apae.api.common.dto.appointment.request.consultation_history;

import jakarta.validation.constraints.NotNull;

public record UpdateConsultationHistoryDTO(

        @NotNull(message = "O indicador de realização é obrigatório") Boolean performed,

        String justification) {
}
