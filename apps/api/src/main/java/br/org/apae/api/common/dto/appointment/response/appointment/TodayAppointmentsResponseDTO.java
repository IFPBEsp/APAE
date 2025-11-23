package br.org.apae.api.common.dto.appointment.response.appointment;

import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record TodayAppointmentsResponseDTO(
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
