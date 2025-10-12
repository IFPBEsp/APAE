package br.org.apae.api.common.dto.appointment.response.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
    UUID id,
    UUID patientId,
    UUID professionalId,
    Integer frequencyDays,
    LocalDate nextAppointmentDate,
    LocalTime nextAppointmentTime,
    Boolean confirmed,
    String description,
    String justification,
    LocalDateTime creationDate) {
}
