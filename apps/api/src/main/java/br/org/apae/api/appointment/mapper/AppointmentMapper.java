package br.org.apae.api.appointment.mapper;

import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
  public Appointment toEntity(CreateAppointmentDTO dto) {
    return new Appointment(
        dto.patientId(),
        dto.professionalId(),
        dto.frequencyDays(),
        dto.nextAppointmentDate(),
        dto.nextAppointmentTime(),
        dto.confirmed(),
        dto.description(),
        null,
        LocalDateTime.now());
  }

  public Appointment updateEntity(Appointment appointment, UpdateAppointmentDTO dto) {
    return new Appointment(
        appointment.getId(),
        dto.patientId() != null ? dto.patientId() : appointment.getPatientId(),
        dto.professionalId() != null ? dto.professionalId() : appointment.getProfessionalId(),
        dto.frequencyDays() != null ? dto.frequencyDays() : appointment.getFrequencyDays(),
        dto.nextAppointmentDate() != null ? dto.nextAppointmentDate() : appointment.getNextAppointment(),
        dto.nextAppointmentTime() != null ? dto.nextAppointmentTime() : appointment.getNextAppointmentTime(),
        dto.confirmed() != null ? dto.confirmed() : appointment.getConfirmed(),
        dto.description() != null ? dto.description() : appointment.getDescription(),
        dto.justification() != null ? dto.justification() : appointment.getJustification(),
        appointment.getCreationDate());
  }

  public AppointmentResponseDTO toResponse(Appointment appointment) {
    return new AppointmentResponseDTO(
        appointment.getId(),
        appointment.getPatientId(),
        appointment.getProfessionalId(),
        appointment.getFrequencyDays(),
        appointment.getNextAppointment(),
        appointment.getNextAppointmentTime(),
        appointment.getConfirmed(),
        appointment.getDescription(),
        appointment.getJustification(),
        appointment.getCreationDate());
  }
}
