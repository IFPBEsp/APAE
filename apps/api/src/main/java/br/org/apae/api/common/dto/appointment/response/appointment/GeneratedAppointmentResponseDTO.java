package br.org.apae.api.common.dto.appointment.response.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;

public record GeneratedAppointmentResponseDTO(
        UUID id,
        UUID appointmentId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime scheduledDateTime,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime overriddenDateTime,

        boolean performed,
        boolean cancelled,
        String cancellationReason,
        UUID patientId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime effectiveDateTime
) {}