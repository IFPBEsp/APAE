package br.org.apae.api.common.dto.appointment.request.consultation_history;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateConsultationHistoryDTO(

        @NotNull(message = "O ID da consulta é obrigatório") UUID appointmentId,

        @NotNull(message = "A data da consulta é obrigatória") @PastOrPresent(message = "A data da consulta não pode estar no futuro") LocalDate consultationDate,

        @NotNull(message = "O horário da consulta é obrigatório") LocalTime consultationTime,

        @NotNull(message = "O indicador de realização é obrigatório") Boolean performed,

        String justification) {
}
