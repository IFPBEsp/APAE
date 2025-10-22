package br.org.apae.api.common.dto.appointment.request.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentDTO(

        @NotNull(message = "O ID do profissional é obrigatório") UUID professionalId,

        @NotNull(message = "A frequência em dias é obrigatória") @Positive(message = "A frequência em dias deve ser maior que zero") Integer frequencyDays,

        @NotNull(message = "A data inicial das consultas não pode ser nula") @FutureOrPresent(message = "A data inicial das consultas não pode estar no passado")LocalDate initialDate,

        @NotNull(message = "A hora das consultas não pode ser nula")  @JsonFormat(pattern = "HH:mm:ss") LocalTime hour,

        @NotNull(message = "A data final das consultas não pode ser nulo") @FutureOrPresent(message = "A data final das consultas não pode estar no passado") LocalDate endDate,

        @NotNull(message = "O ID do atendimento não pode ser nulo") UUID serviceId,

        @NotNull(message = "O ID  do registro anual não pode estar em branco") UUID annualRegistrationId) {
}
