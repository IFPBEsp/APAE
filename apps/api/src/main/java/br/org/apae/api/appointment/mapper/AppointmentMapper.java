package br.org.apae.api.appointment.mapper;

import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;

import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
  public Appointment toEntity(CreateAppointmentDTO dto) {
    return new Appointment(
            dto.professionalId(),
            dto.frequencyDays(),
            dto.initialDate(),
            dto.hour(),
            dto.endDate(),
            dto.serviceId(),
            dto.annualRegistrationId()
    );
  }

  public Appointment updateEntity(Appointment appointment, UpdateAppointmentDTO dto) {
    return new Appointment(
        appointment.getId(),
        dto.professionalId() != null ? dto.professionalId() : appointment.getProfessionalId(),
        dto.frequencyDays() != null ? dto.frequencyDays() : appointment.getFrequencyDays(),
        dto.initialDate() != null ? dto.initialDate() : appointment.getInitialDate(),
        dto.hour() != null ? dto.hour() : appointment.getHour(),
        dto.endDate() != null ? dto.endDate() : appointment.getEndDate(),
        dto.serviceId() != null ? dto.serviceId() : appointment.getServiceId(),
        dto.annualRegistrationId() != null ? dto.annualRegistrationId() : appointment.getAnnualRegistrationId(),
        appointment.getCreationDate());
  }

  public AppointmentResponseDTO toResponse(Appointment appointment) {
    return new AppointmentResponseDTO(
        appointment.getId(),
        appointment.getProfessionalId(),
        appointment.getFrequencyDays(),
        appointment.getInitialDate(),
        appointment.getHour(),
        appointment.getEndDate(),
        appointment.getServiceId(),
        appointment.getAnnualRegistrationId(),
        appointment.getCreationDate()
    );
  }
}
