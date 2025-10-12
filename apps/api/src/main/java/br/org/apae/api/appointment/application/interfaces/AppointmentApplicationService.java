package br.org.apae.api.appointment.application.interfaces;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;

public interface AppointmentApplicationService {
  void create(CreateAppointmentDTO dto);

  Page<AppointmentResponseDTO> findAll(Pageable pageable);

  Page<AppointmentResponseDTO> findAllByDate(LocalDate date, Pageable pageable);

  Page<AppointmentResponseDTO> findAllByDateAndTime(LocalDate date, LocalTime time, Pageable pageable);

  AppointmentResponseDTO findById(UUID id);

  AppointmentResponseDTO update(UUID id, UpdateAppointmentDTO dto);

  void delete(UUID id);
}
