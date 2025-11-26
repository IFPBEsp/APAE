package br.org.apae.api.appointment.mapper;

import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;


import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.TodayAppointmentsResponseDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentMapper {

  public Appointment toEntity(CreateAppointmentDTO dto, HealthProfessional professional, AnnualRegistry annualRegistry) {
    return new Appointment(
        professional,
        dto.serviceId(),
        annualRegistry,
        dto.frequencyDays(),
        dto.hour(),
        dto.initialDate(),
        null
    );
  }

  public Appointment updateEntity(Appointment appointment, UpdateAppointmentDTO dto, HealthProfessional professional, AnnualRegistry annualRegistry) {
    appointment.setProfessional(
        dto.professionalId() != null ? professional : appointment.getProfessional()
    );
    appointment.setServiceId(
        dto.serviceId() != null ? dto.serviceId() : appointment.getServiceId()
    );

    appointment.setAnnualRegistration(
        dto.annualRegistrationId() != null ? annualRegistry : appointment.getAnnualRegistration()
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

  public GeneratedAppointmentResponseDTO toGeneratedResponse(GeneratedAppointment entity) {
    return new GeneratedAppointmentResponseDTO(
            entity.getId(),
            entity.getAppointment().getId(),
            entity.getScheduledDateTime(),
            entity.getOverriddenDateTime(),
            entity.getPerformed(),
            entity.getCancelled(),
            entity.getCancellationReason(),
            entity.getPatientId(),
            entity.getEffectiveDateTime()
    );
  }

  public AppointmentResponseDTO toResponse(Appointment appointment) {
    return new AppointmentResponseDTO(
        appointment.getId(),
        new HealthProfessionalResponseDTO(appointment.getProfessional()),
        appointment.getServiceId(),
        toResponse(appointment.getAnnualRegistration()),
        appointment.getFrequencyDays(),
        appointment.getInitialDate(),
        appointment.getEndDate(),
        appointment.getHour(),
        appointment.isActive(),
        appointment.getCreationDate()
    );
  }

  public AnnualRegistryResponseDTO toResponse(AnnualRegistry annualRegistry) {
    List<DisorderResponseDTO> disorderResponseDTOS = annualRegistry.getDisorders()
        .stream()
        .map(disorder -> new DisorderResponseDTO(disorder.getId(), disorder.getName())).toList();

    return new AnnualRegistryResponseDTO(
        annualRegistry.getId(),
        annualRegistry.getBpc(),
        annualRegistry.getDiseases(),
        annualRegistry.getFamilyIncome(),
        annualRegistry.getYear(),
        new PatientResponseDTO(annualRegistry.getPatient()),
        disorderResponseDTOS
    );
  }

  public TodayAppointmentsResponseDTO toTodayResponseDTO(GeneratedAppointment generatedAppointment) {
    Appointment appointment = generatedAppointment.getAppointment();
    Patient patient = appointment.getAnnualRegistration().getPatient();
    HealthProfessional professional = appointment.getProfessional();
    return new TodayAppointmentsResponseDTO(
        generatedAppointment.getId(),
        new PatientResponseDTO(patient),
        new HealthProfessionalResponseDTO(professional),
        generatedAppointment.getScheduledDateTime(),
        generatedAppointment.getOverriddenDateTime(),
        generatedAppointment.getPerformed(),
        generatedAppointment.getCancelled(),
        generatedAppointment.getCancellationReason(),
        generatedAppointment.getEffectiveDateTime(),
        appointment.getId()
    );
  }
}