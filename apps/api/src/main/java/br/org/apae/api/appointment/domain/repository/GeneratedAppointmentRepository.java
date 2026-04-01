package br.org.apae.api.appointment.domain.repository;

import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeneratedAppointmentRepository extends JpaRepository<GeneratedAppointment, UUID> {

    Optional<GeneratedAppointment> findByAppointmentIdAndScheduledDateTime(UUID appointmentId, LocalDateTime dateTime);
    @Modifying
    @Query("DELETE FROM GeneratedAppointment ga WHERE ga.appointment.id = :appointmentId AND ga.scheduledDateTime >= :cutoff")
    void deleteFutureByAppointmentId(UUID appointmentId, LocalDateTime cutoff);

    Page<GeneratedAppointment> findByPatientIdAndScheduledDateTimeBetween(
            UUID patientId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT g FROM GeneratedAppointment g " +
       "WHERE FUNCTION('DATE', COALESCE(g.overriddenDateTime, g.scheduledDateTime)) = :date")
    Page<GeneratedAppointment> listAppointmentsForToday(@Param("date") java.time.LocalDate date, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GeneratedAppointment g " +
       "WHERE g.patientId = :patientId " +
       "AND g.appointment.professional.id = :professionalId " +
       "AND FUNCTION('DATE', COALESCE(g.overriddenDateTime, g.scheduledDateTime)) = :date " +
       "AND (g.cancelled IS NULL OR g.cancelled = false) " +
       "AND (:excludeAppointmentId IS NULL OR g.appointment.id <> :excludeAppointmentId)")
boolean existsConflictForPatientAndProfessional(
        @Param("patientId") UUID patientId,
        @Param("professionalId") UUID professionalId,
        @Param("date") LocalDate date,
        @Param("excludeAppointmentId") UUID excludeAppointmentId
);
}
