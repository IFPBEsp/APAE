package br.org.apae.api.common.dto.appointment.response.appointment;

import java.time.LocalDateTime;
import java.util.UUID;

import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;

public record TodayAppointmentsResponseDTO(
  UUID id,
  PatientResponseDTO patient,
  HealthProfessionalResponseDTO professional,
  LocalDateTime scheduledDateTime,
  LocalDateTime overriddenDateTime,
  Boolean performed,
  Boolean cancelled,
  String cancellationReason,
  LocalDateTime effectiveDateTime,
  UUID ruleId
) {}
