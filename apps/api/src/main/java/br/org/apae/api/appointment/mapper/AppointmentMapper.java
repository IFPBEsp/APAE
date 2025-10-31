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
        dto.serviceId(),
        dto.annualRegistrationId(),
        dto.frequencyDays(),
        dto.hour(),
        dto.initialDate(),
        dto.endDate()
    );
  }

  public Appointment updateEntity(Appointment appointment, UpdateAppointmentDTO dto) {
    appointment.setProfessionalId(
        dto.professionalId() != null ? dto.professionalId() : appointment.getProfessionalId()
    );
    appointment.setAnnualRegistrationId(
        dto.annualRegistrationId() != null ? dto.annualRegistrationId() : appointment.getAnnualRegistrationId()
    );
    appointment.setServiceId(
        dto.serviceId() != null ? dto.serviceId() : appointment.getServiceId()
    );

    appointment.setHour(
        dto.hour() != null ? dto.hour() : appointment.getHour()
    );
    appointment.setFrequencyDays(
        dto.frequencyDays() != null ? dto.frequencyDays() : appointment.getFrequencyDays()
    );
    appointment.setInitialDate(
        dto.initialDate() != null ? dto.initialDate() : appointment.getInitialDate()
    );
    appointment.setEndDate(
        dto.endDate() != null ? dto.endDate() : appointment.getEndDate()
    );
    return appointment;
  }

  public AppointmentResponseDTO toResponse(Appointment appointment) {
    return new AppointmentResponseDTO(
        appointment.getId(),
        appointment.getProfessionalId(),
        appointment.getServiceId(),
        appointment.getAnnualRegistrationId(),
        appointment.getFrequencyDays(),
        appointment.getInitialDate(),
        appointment.getEndDate(),
        appointment.getHour(),
        appointment.isActive(),
        appointment.getCreationDate()
    );
  }
}