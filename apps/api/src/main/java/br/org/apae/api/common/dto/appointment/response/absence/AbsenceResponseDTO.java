package br.org.apae.api.common.dto.appointment.response.absence;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Dados de resposta para uma Falta (Absence) registrada.")
public record AbsenceResponseDTO(
        UUID id,
        @Schema(description = "ID do Agendamento Gerado ao qual a falta está vinculada.")
        UUID generatedAppointmentId,
        @Schema(description = "ID do Paciente associado.")
        UUID patientId,
        @Schema(description = "ID do Profissional associado (do Appointment de origem).")
        UUID professionalId,
        LocalDate absenceDate,
        String justification,
        Boolean notified,
        Boolean isJustified,
        @Schema(description = "ID do documento de justificativa associado a falta.")
        String justificationDocumentId
) {
}