package br.org.apae.api.common.dto.appointment.request.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentDTO(

    @NotNull(message = "O ID do profissional é obrigatório")
    UUID professionalId,

    @NotNull(message = "O ID do atendimento é obrigatório")
    UUID serviceId,

    @NotNull(message = "O ID do cadastro anual é obrigatório")
    UUID annualRegistration,

    @NotNull(message = "A frequência em dias é obrigatória")
    @Positive(message = "A frequência de dias deve ser maior que 0")
    Integer frequencyDays,

    @NotNull(message = "A data inicial é obrigatória")
    @FutureOrPresent(message = "A data inicial não pode ser no passado")
    LocalDate initialDate,

    @NotNull(message = "A hora é obrigatória")
    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime hour
) {}
