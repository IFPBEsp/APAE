package br.org.apae.api.controllers.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.interfaces.controllers.AppointmentController;
import br.org.apae.api.common.dto.appointment.request.appointment.*;
import br.org.apae.api.common.dto.appointment.response.appointment.*;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentControllerImpl implements AppointmentController {

  private final AppointmentApplicationService service;

  public AppointmentControllerImpl(AppointmentApplicationService service) {
    this.service = service;
  }

  @Override
  public ResponseEntity<Void> create(@Valid CreateAppointmentDTO dto) {
    service.create(dto);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Page<AppointmentResponseDTO>> getAll(LocalDate date, LocalTime time, Pageable pageable) {
    Page<AppointmentResponseDTO> result = service.findAll(date, time, pageable);
    return ResponseEntity.ok(result);
  }

  @Override
  public ResponseEntity<AppointmentResponseDTO> get(UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @Override
  public ResponseEntity<AppointmentResponseDTO> updateRule(UUID id, @Valid UpdateAppointmentRuleDTO dto) {
    AppointmentResponseDTO updated = service.updateAppointment(
            id,
            dto.newFrequency(),
            dto.newTime()
    );
    return ResponseEntity.ok(updated);
  }

  @Override
  public ResponseEntity<Void> delete(UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<GeneratedAppointmentResponseDTO> reschedule(UUID id, @Valid RescheduleGeneratedAppointmentDTO dto) {
    GeneratedAppointmentResponseDTO rescheduled = service.reschedule(id, dto.newDateTime());
    return ResponseEntity.ok(rescheduled);
  }

  @Override
  public ResponseEntity<GeneratedAppointmentResponseDTO> markAsPerformed(UUID id) {
    return ResponseEntity.ok(service.markAsPerformed(id));
  }

  @Override
  public ResponseEntity<GeneratedAppointmentResponseDTO> cancel(UUID id, @Valid CancelGeneratedAppointmentDTO dto) {
    return ResponseEntity.ok(service.cancel(id, dto.reason()));
  }

  @Override
  public ResponseEntity<Page<GeneratedAppointmentResponseDTO>> listByPatient(
          UUID patientId, LocalDate start, LocalDate end, Pageable pageable) {
    Page<GeneratedAppointmentResponseDTO> page = service.listByPatient(patientId, start, end, pageable);
    return ResponseEntity.ok(page);
  }

  @Override
  public ResponseEntity<Page<TodayAppointmentsResponseDTO>> listTodayAppointment(Pageable pageable) {
    return ResponseEntity.ok(this.service.listAppointmentForToday(pageable));
  }
}