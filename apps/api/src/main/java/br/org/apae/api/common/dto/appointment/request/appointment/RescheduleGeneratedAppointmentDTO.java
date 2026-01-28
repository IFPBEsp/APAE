package br.org.apae.api.common.dto.appointment.request.appointment;

import java.time.LocalDateTime;

public record RescheduleGeneratedAppointmentDTO(
        LocalDateTime newDateTime
) {}