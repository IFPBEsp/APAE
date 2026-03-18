package br.org.apae.api.appointment.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.org.apae.api.appointment.domain.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

  Page<Appointment> findAllByInitialDate(LocalDate date, Pageable pageable);

  Page<Appointment> findAllByInitialDateAndHour(LocalDate date, LocalTime hour,
      Pageable pageable);

  List<Appointment> findByAnnualRegistrationIdAndIsActiveTrue(UUID registrationId);
  Optional<Appointment> findByAnnualRegistrationIdAndIsActiveTrueOrderByInitialDateDesc(UUID registrationId);

  // TODO: Criar query para validar se horário definido já está utilizado
  @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.professional.id = :professionalId AND a.initialDate = :initialDate AND a.hour = :hour AND a.isActive = true")
  boolean existsConflict(
          @Param("professionalId") UUID professionalId,
          @Param("initialDate") LocalDate initialDate,
          @Param("hour") LocalTime hour
  );
}
