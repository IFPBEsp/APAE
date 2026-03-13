package br.org.apae.api.appointment.domain.repository;

import br.org.apae.api.appointment.domain.model.Absence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {

    Optional<Absence> findByGeneratedAppointmentId(UUID generatedAppointmentId);

    Page<Absence> findByGeneratedAppointmentId(UUID generatedId, Pageable pageable);

    @Query("SELECT a FROM Absence a JOIN a.generatedAppointment ga WHERE ga.patientId = :patientId")
    Page<Absence> findByPatientId(UUID patientId, Pageable pageable);

    @Query("""
        SELECT a FROM Absence a 
        JOIN a.generatedAppointment ga 
        JOIN ga.appointment app 
        WHERE app.professional.id = :professionalId
    """)
    Page<Absence> findByProfessionalId(UUID professionalId, Pageable pageable);

    @Query("""
        SELECT a FROM Absence a 
        JOIN a.generatedAppointment ga 
        JOIN ga.appointment app 
        WHERE (:generatedId IS NULL OR ga.id = :generatedId)
          AND (:patientId IS NULL OR ga.patientId = :patientId)
          AND (:professionalId IS NULL OR app.professional.id = :professionalId)
    """)
    Page<Absence> findByFilters(
            UUID generatedId,
            UUID patientId,
            UUID professionalId,
            Pageable pageable
    );

}