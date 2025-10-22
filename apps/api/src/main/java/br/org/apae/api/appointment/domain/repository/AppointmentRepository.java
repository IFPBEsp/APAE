package br.org.apae.api.appointment.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.appointment.domain.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

  Page<Appointment> findAllByInitialDate(LocalDate date, Pageable pageable);

  Page<Appointment> findAllByInitialDateAndHour(LocalDate date, LocalTime time,
      Pageable pageable);
}
