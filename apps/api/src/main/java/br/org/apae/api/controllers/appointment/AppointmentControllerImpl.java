package br.org.apae.api.controllers.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.interfaces.controllers.AppointmentController;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import jakarta.validation.Valid;

@RestController
public class AppointmentControllerImpl implements AppointmentController {
  private final AppointmentApplicationService appointmentApplicationService;

  public AppointmentControllerImpl(AppointmentApplicationService appointmentApplicationService) {
    this.appointmentApplicationService = appointmentApplicationService;
  }

  @Override
  public ResponseEntity<Void> create(@Valid CreateAppointmentDTO dto) {
    appointmentApplicationService.create(dto);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Page<AppointmentResponseDTO>> getAll(LocalDate date, LocalTime time, Pageable pageable) {
    if (date != null && time == null) {
      return ResponseEntity.ok(appointmentApplicationService.findAllByDate(date, pageable));
    } else if (date != null) {
      return ResponseEntity.ok(appointmentApplicationService.findAllByDateAndTime(date, time, pageable));
    }
    return ResponseEntity.ok(appointmentApplicationService.findAll(pageable));
  }

  @Override
  public ResponseEntity<AppointmentResponseDTO> get(UUID id) {
    AppointmentResponseDTO appointment = appointmentApplicationService.findById(id);
    return ResponseEntity.ok(appointment);
  }

  @Override
  public ResponseEntity<AppointmentResponseDTO> update(UUID id, @Valid UpdateAppointmentDTO dto) {
    AppointmentResponseDTO appointmentUpdated = appointmentApplicationService.update(id, dto);
    return ResponseEntity.ok(appointmentUpdated);
  }

  @Override
  public ResponseEntity<Void> delete(UUID id) {
    appointmentApplicationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
