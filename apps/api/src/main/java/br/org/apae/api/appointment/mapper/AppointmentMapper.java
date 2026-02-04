package br.org.apae.api.appointment.mapper;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Component;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.TodayAppointmentsResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.professional.domain.model.HealthProfessional;

@Component
public class AppointmentMapper {

  public Appointment toEntity(CreateAppointmentDTO dto, HealthProfessional professional, AnnualRegistry annualRegistry) {
    return new Appointment(
        professional,
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

  public AppointmentResponseDTO toResponse(Appointment appointment, PatientResponseDTO patient) {
    HealthProfessional professional = appointment.getProfessional();
    ServiceAreaResponseDTO serviceArea = new ServiceAreaResponseDTO(professional.getServiceArea());
    AddressResponseDTO address = new AddressResponseDTO(professional.getAddress());
    List<AvailabilityResponseDTO> availabilities = professional.getAvailabilities()
        .stream()
        .map(AvailabilityResponseDTO::new).toList();

    return new AppointmentResponseDTO(
        appointment.getId(),
        new HealthProfessionalResponseDTO(
          professional, 
          serviceArea,
          address, 
          availabilities),
        toResponse(appointment.getAnnualRegistration(), patient),
        appointment.getFrequencyDays(),
        appointment.getInitialDate(),
        appointment.getEndDate(),
        appointment.getHour(),
        appointment.isActive(),
        appointment.getCreationDate()
    );
  }

  public AnnualRegistryResponseDTO toResponse(AnnualRegistry annualRegistry, PatientResponseDTO patient) {
    List<DisorderResponseDTO> disorderResponseDTOS = annualRegistry.getDisorders()
        .stream()
        .map(disorder -> new DisorderResponseDTO(disorder.getId(), disorder.getName())).toList();

    return new AnnualRegistryResponseDTO(
        annualRegistry.getId(),
        annualRegistry.getBpc(),
        annualRegistry.getDiseases(),
        annualRegistry.getFamilyIncome(),
            Year.of(annualRegistry.getYear()),
        patient,
        disorderResponseDTOS
    );
  }

  public TodayAppointmentsResponseDTO toTodayResponseDTO(GeneratedAppointment generatedAppointment, PatientResponseDTO patient, Boolean hasAbsence) {
    Appointment appointment = generatedAppointment.getAppointment();
    // Patient patient = appointment.getAnnualRegistration().getPatient();
    HealthProfessional professional = appointment.getProfessional();
    ServiceAreaResponseDTO serviceArea = new ServiceAreaResponseDTO(professional.getServiceArea());
    AddressResponseDTO address = new AddressResponseDTO(professional.getAddress());
    List<AvailabilityResponseDTO> availabilities = professional.getAvailabilities()
        .stream()
        .map(AvailabilityResponseDTO::new).toList();

    return new TodayAppointmentsResponseDTO(
        generatedAppointment.getId(),
        patient,
        new HealthProfessionalResponseDTO(
          professional, 
          serviceArea,
          address, 
          availabilities),
        generatedAppointment.getScheduledDateTime(),
        generatedAppointment.getOverriddenDateTime(),
        generatedAppointment.getPerformed(),
        generatedAppointment.getCancelled(),
        generatedAppointment.getCancellationReason(),
        generatedAppointment.getEffectiveDateTime(),
        appointment.getId(), hasAbsence
    );
  }
}