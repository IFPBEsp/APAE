package br.org.apae.api.common.dto.appointment.request.appointment;

import java.time.LocalTime;

public record UpdateAppointmentRuleDTO(
        Integer newFrequency,
        LocalTime newTime
) {}