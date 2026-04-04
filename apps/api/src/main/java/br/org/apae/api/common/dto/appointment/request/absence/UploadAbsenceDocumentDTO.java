package br.org.apae.api.common.dto.appointment.request.absence;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para upload de documento de justificativa de falta.")
public record UploadAbsenceDocumentDTO(

        @Schema(description = "ID do agendamento gerado vinculado à falta.")
        @NotNull(message = "O ID do agendamento é obrigatório.")
        UUID generatedAppointmentId,

        @Schema(description = "Tipo do documento.")
        @NotNull(message = "O tipo do documento é obrigatório.")
        String type

) {}