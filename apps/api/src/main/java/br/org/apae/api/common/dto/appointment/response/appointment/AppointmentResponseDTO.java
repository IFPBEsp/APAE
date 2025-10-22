package br.org.apae.api.common.dto.appointment.response.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
    UUID id,
    UUID professionalId,
    Integer frequencyDays,
    LocalDate initialDate,
    LocalTime hour,
    LocalDate endDate,
    UUID serviceId,
    UUID annualRegistrationId,
    LocalDateTime creationDate ){
}

