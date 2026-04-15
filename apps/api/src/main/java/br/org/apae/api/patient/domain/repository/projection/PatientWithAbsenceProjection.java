package br.org.apae.api.patient.domain.repository.projection;

import br.org.apae.api.patient.domain.model.Patient;

import java.time.LocalDateTime;

public interface PatientWithAbsenceProjection {
    Patient getPatient();
    Long getAbsenceCount();
    LocalDateTime getLastAbsenceDate();
}
