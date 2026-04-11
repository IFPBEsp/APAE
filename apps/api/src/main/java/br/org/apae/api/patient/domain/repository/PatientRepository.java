package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.repository.projection.PatientWithAbsenceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
  boolean existsByCpfOrRg(String cpf, String rg);

    @Query("SELECT DISTINCT p.address.city FROM Patient p WHERE p.address.city IS NOT NULL AND p.address.city <> '' ORDER BY p.address.city ASC")
    List<String> findDistinctCities();

    @Query("""
      SELECT p as patient,
             COUNT(a) as absenceCount,
             MAX(COALESCE(a.generatedAppointment.overriddenDateTime, a.generatedAppointment.scheduledDateTime)) as lastAbsenceDate
      FROM Patient p
      LEFT JOIN Absence a
          ON a.generatedAppointment.patientId = p.id
      GROUP BY p
      HAVING COUNT(a) >= COALESCE(:minAbsences, 0)
    """)
    Page<PatientWithAbsenceProjection> findPatientsWithAbsences(
            Integer minAbsences,
            Pageable pageable
    );

    @Query("SELECT COUNT(p) > 0 FROM Patient p JOIN p.vaccines v WHERE v.id = :vaccineId")
    boolean isVaccineInUse(UUID vaccineId);

    @Query("SELECT DISTINCT v.id FROM Patient p JOIN p.vaccines v WHERE p.id = :patientId")
    List<UUID> findVaccineIdsByPatientId(UUID patientId);
}
