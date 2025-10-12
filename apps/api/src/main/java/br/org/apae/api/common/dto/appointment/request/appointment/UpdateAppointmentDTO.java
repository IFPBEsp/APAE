package br.org.apae.api.common.dto.appointment.request.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;

public record UpdateAppointmentDTO(
    UUID patientId,
    UUID professionalId,
    Integer frequencyDays,
    LocalDate nextAppointmentDate,

    @JsonFormat(pattern = "HH:mm:ss") LocalTime nextAppointmentTime,

    Boolean confirmed,

    @NotBlank(message = "Description cannot be blank") String description,

    String justification) {
}
