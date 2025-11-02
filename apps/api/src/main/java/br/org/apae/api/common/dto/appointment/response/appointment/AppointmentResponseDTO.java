package br.org.apae.api.common.dto.appointment.response.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
  UUID id,
  UUID professionalId,
  UUID serviceId,
  UUID annualRegistrationId,
  Integer frequencyDays,
  LocalDate initialDate,
  LocalDate endDate,
  LocalTime hour,
  boolean isActive,
  LocalDateTime creationDate
) {}
