package br.org.apae.api.appointment.application.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.appointment.request.appointment.*;
import br.org.apae.api.common.dto.appointment.response.appointment.*;

public interface AppointmentApplicationService {
  void create(CreateAppointmentDTO dto);

  Page<AppointmentResponseDTO> findAll(Pageable pageable);

  Page<AppointmentResponseDTO> findAll(LocalDate date, LocalTime time, Pageable pageable);

  Page<AppointmentResponseDTO> findAllByDate(LocalDate date, Pageable pageable);

  Page<AppointmentResponseDTO> findAllByDateAndTime(LocalDate date, LocalTime time, Pageable pageable);

  AppointmentResponseDTO findById(UUID id);

  AppointmentResponseDTO updateAppointment(UUID appointmentId, Integer newFrequency, LocalTime newTime);

  void delete(UUID id);

  GeneratedAppointmentResponseDTO reschedule(UUID generatedId, LocalDateTime newDateTime);

  GeneratedAppointmentResponseDTO markAsPerformed(UUID generatedId);

  GeneratedAppointmentResponseDTO cancel(UUID generatedId, String reason);

  Page<GeneratedAppointmentResponseDTO> listByPatient(
          UUID patientId, LocalDate start, LocalDate end, Pageable pageable);

  List<GeneratedAppointmentResponseDTO> generateAppointments(
          UUID annualRegistrationId, LocalDate start, LocalDate end);

  Page<TodayAppointmentsResponseDTO> listAppointmentForToday(Pageable pageable);

  TodayAppointmentsResponseDTO findGeneratedAppointmentById(UUID id);
}