package br.org.apae.api.common.dto.appointment.request.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentDTO(

        @NotNull(message = "O ID do paciente não pode ser nulo") UUID patientId,

        @NotNull(message = "O ID do profissional não pode ser nulo") UUID professionalId,

        @NotNull(message = "A frequência em dias não pode ser nula") Integer frequencyDays,

        @NotNull(message = "A data da próxima consulta não pode ser nula") LocalDate nextAppointmentDate,

        @NotNull(message = "O horário da próxima consulta não pode ser nulo") @JsonFormat(pattern = "HH:mm:ss") LocalTime nextAppointmentTime,

        @NotNull(message = "O status de confirmação não pode ser nulo") Boolean confirmed,

        @NotBlank(message = "A descrição não pode estar em branco") String description) {
}
