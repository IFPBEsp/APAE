package br.org.apae.api.common.dto.patient.response.patient;

import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record PatientWithAbsencesResponseDTO(
        PatientSummaryResponseDTO patient,
        Long absenceCount,
        LocalDateTime lastAbsenceDate,
        List<AbsenceResponseDTO> absences
) {}
