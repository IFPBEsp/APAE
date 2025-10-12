package br.org.apae.api.common.dto.appointment.request.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAppointmentDTO(

    @NotNull(message = "Patient ID cannot be null") UUID patientId,

    @NotNull(message = "Professional ID cannot be null") UUID professionalId,

    @NotNull(message = "Frequency in days cannot be null") Integer frequencyDays,

    @NotNull(message = "Next appointment date cannot be null") LocalDate nextAppointmentDate,

    @NotNull(message = "Next appointment time cannot be null") @JsonFormat(pattern = "HH:mm:ss") LocalTime nextAppointmentTime,

    @NotNull(message = "Confirmation status cannot be null") Boolean confirmed,

    @NotBlank(message = "Description cannot be blank") String description) {
}
