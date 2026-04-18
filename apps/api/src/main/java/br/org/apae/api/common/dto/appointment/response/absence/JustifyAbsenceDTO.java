package br.org.apae.api.common.dto.appointment.response.absence;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados enviados para justificar uma falta existente.")
public record JustifyAbsenceDTO(
        @NotBlank(message = "A justificativa é obrigatória.")
        @Schema(description = "Texto descrevendo o motivo da justificativa.")
        String justification,
        String justificationDocumentId
) {
}
