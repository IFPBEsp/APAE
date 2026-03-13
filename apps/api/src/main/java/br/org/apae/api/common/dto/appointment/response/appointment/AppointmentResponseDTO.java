package br.org.apae.api.common.dto.appointment.response.appointment;

import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
  UUID id,
  HealthProfessionalResponseDTO professional,
  AnnualRegistryResponseDTO annualRegistration,
  Integer frequencyDays,
  LocalDate initialDate,
  LocalDate endDate,
  LocalTime hour,
  boolean isActive,
  LocalDateTime creationDate
) {}
