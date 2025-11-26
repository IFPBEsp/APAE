package br.org.apae.api.common.dto.appointment.request.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateAppointmentDTO(

        @NotNull(message = "O ID do paciente é obrigatório") UUID patientId,

        @NotNull(message = "O ID do profissional é obrigatório") UUID professionalId,

        @NotNull(message = "A frequência em dias é obrigatória") @Positive(message = "A frequência em dias deve ser maior que zero") Integer frequencyDays,

        @NotNull(message = "A data da próxima consulta é obrigatória") @FutureOrPresent(message = "A data da próxima consulta não pode estar no passado") LocalDate nextAppointmentDate,

        @NotNull(message = "O horário da próxima consulta é obrigatório") @JsonFormat(pattern = "HH:mm:ss") LocalTime nextAppointmentTime,

        @NotNull(message = "O indicador de confirmação é obrigatório") Boolean confirmed,

        @NotBlank(message = "A descrição não pode estar em branco") String description,

        String justification) {
}
