package br.org.apae.api.common.dto.appointment.request.absence;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados para o registro de uma nova Falta (Absence).")
public record CreateAbsenceDTO(
        @Schema(description = "ID do Agendamento Gerado (GeneratedAppointment) ao qual a falta está vinculada.")
        @NotNull(message = "O ID do agendamento gerado é obrigatório.")
        UUID generatedAppointmentId,

        @Schema(description = "Data em que a falta ocorreu/será registrada. Deve ser a data do agendamento efetivo.")
        @NotNull(message = "A data da falta é obrigatória.")
        LocalDate absenceDate,

        @Schema(description = "Status de justificativa da ausência.")
        String isJustified,

        @Schema(description = "Justificativa da falta.", nullable = true, example = "Motivo de saúde urgente.")
        String justification,

        @Schema(description = "Documento para justificativa da falta.", nullable = true)
        String justificationDocumentId
) {
}