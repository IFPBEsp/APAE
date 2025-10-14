package br.org.apae.api.appointment.domain.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.appointment.domain.model.ConsultationHistory;

@Repository
public interface ConsultationHistoryRepository extends JpaRepository<ConsultationHistory, UUID> {
  boolean existsByAppointmentIdAndConsultationDateAndConsultationTime(UUID appointmentId, LocalDate consultationDate,
      LocalTime consultationTime);

  Page<ConsultationHistoryRepository> findAllByConsultationDate(LocalDate consultationDate, Pageable pageable);
}
