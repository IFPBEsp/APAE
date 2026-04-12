package br.org.apae.api.appointment.domain.repository;

import br.org.apae.api.appointment.domain.model.Absence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {

    @EntityGraph(attributePaths = {"generatedAppointment"})
    Optional<Absence> findByGeneratedAppointmentId(UUID generatedAppointmentId);

    @EntityGraph(attributePaths = {"generatedAppointment"})
    Page<Absence> findByGeneratedAppointmentId(UUID generatedId, Pageable pageable);

    @EntityGraph(attributePaths = {"generatedAppointment"})
    @Query("SELECT a FROM Absence a WHERE a.generatedAppointment.patientId = :patientId")
    Page<Absence> findByPatientId(UUID patientId, Pageable pageable);

    @EntityGraph(attributePaths = {"generatedAppointment", "generatedAppointment.appointment"})
    @Query("""
        SELECT a FROM Absence a 
        WHERE a.generatedAppointment.appointment.professional.id = :professionalId
    """)
    Page<Absence> findByProfessionalId(UUID professionalId, Pageable pageable);

    @EntityGraph(attributePaths = {"generatedAppointment"})
    @Query("""
        SELECT a FROM Absence a
        WHERE a.generatedAppointment.patientId IN :patientIds
    """)
    List<Absence> findByPatientIds(List<UUID> patientIds);

    @EntityGraph(attributePaths = {"generatedAppointment", "generatedAppointment.appointment"})
    @Query("""
        SELECT a FROM Absence a 
        WHERE (:generatedId IS NULL OR a.generatedAppointment.id = :generatedId)
          AND (:patientId IS NULL OR a.generatedAppointment.patientId = :patientId)
          AND (:professionalId IS NULL OR a.generatedAppointment.appointment.professional.id = :professionalId)
    """)
    Page<Absence> findByFilters(
            UUID generatedId,
            UUID patientId,
            UUID professionalId,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(sub.patientId)
        FROM (
            SELECT a.generatedAppointment.patientId AS patientId
            FROM Absence a
            GROUP BY a.generatedAppointment.patientId
            HAVING COUNT(a) >= :minAbsences
        ) sub
    """)
    long countPatientsWithAbsences(@Param("minAbsences") int minAbsences);
}
