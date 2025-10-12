package br.org.apae.api.appointment.application.internal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.repository.AppointmentRepository;
import br.org.apae.api.appointment.mapper.AppointmentMapper;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;

@Service
public class AppointmentApplicationServiceImpl implements AppointmentApplicationService {
  private final AppointmentRepository appointmentRepository;
  private final AppointmentMapper appointmentMapper;

  public AppointmentApplicationServiceImpl(AppointmentRepository appointmentRepository,
      AppointmentMapper appointmentMapper) {
    this.appointmentRepository = appointmentRepository;
    this.appointmentMapper = appointmentMapper;
  }

  @Override
  public void create(CreateAppointmentDTO dto) {
    Appointment appointment = this.appointmentMapper.toEntity(dto);
    this.appointmentRepository.save(appointment);
  }

  @Override
  public Page<AppointmentResponseDTO> findAll(Pageable pageable) {
    return this.appointmentRepository.findAll(pageable).map(this.appointmentMapper::toResponse);
  }

  @Override
  public Page<AppointmentResponseDTO> findAllByDate(LocalDate date, Pageable pageable) {
    return this.appointmentRepository.findAllByNextAppointment(date, pageable)
        .map(this.appointmentMapper::toResponse);
  }

  @Override
  public Page<AppointmentResponseDTO> findAllByDateAndTime(LocalDate date, LocalTime time, Pageable pageable) {
    return this.appointmentRepository.findAllByNextAppointmentAndNextAppointmentTime(date, time, pageable)
        .map(this.appointmentMapper::toResponse);
  }

  @Override
  public AppointmentResponseDTO findById(UUID id) {
    Appointment appointment = this.appointmentRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);

    return this.appointmentMapper.toResponse(appointment);
  }

  @Override
  public AppointmentResponseDTO update(UUID id, UpdateAppointmentDTO dto) {
    Appointment appointment = this.appointmentRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);

    Appointment appointmentToUpdate = this.appointmentMapper.updateEntity(appointment, dto);
    Appointment updated = this.appointmentRepository.save(appointmentToUpdate);

    return this.appointmentMapper.toResponse(updated);
  }

  @Override
  public void delete(UUID id) {
    if (!this.appointmentRepository.existsById(id)) {
      throw new AppointmentNotFoundException();
    }

    this.appointmentRepository.deleteById(id);
  }
}
