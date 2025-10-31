package br.org.apae.api.common.dto.appointment.request.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;

public record UpdateAppointmentDTO(

    UUID professionalId,
    AnnualRegistry annualRegistrationId,
    UUID serviceId,

    @Positive(message = "A frequência de dias deve ser maior que 0")
    Integer frequencyDays,

    @FutureOrPresent(message = "A data inicial não pode ser no passado")
    LocalDate initialDate,

    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime hour,

    @FutureOrPresent(message = "A data final não pode ser no passado")
    LocalDate endDate
) {}