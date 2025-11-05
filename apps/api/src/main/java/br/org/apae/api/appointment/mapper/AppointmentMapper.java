package br.org.apae.api.appointment.mapper;

import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppointmentMapper {

    public Appointment toEntity(CreateAppointmentDTO dto) {
        return new Appointment(
                dto.professionalId(),
                dto.serviceId(),
                null,
                dto.frequencyDays(),
                dto.hour(),
                dto.initialDate(),
                null
        );
    }

    public Appointment updateEntity(Appointment appointment, UpdateAppointmentDTO dto) {
        appointment.setProfessionalId(
                dto.professionalId() != null ? dto.professionalId() : appointment.getProfessionalId()
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
                appointment.getAnnualRegistration(),
                appointment.getFrequencyDays(),
                appointment.getInitialDate(),
                appointment.getEndDate(),
                appointment.getHour(),
                appointment.isActive(),
                appointment.getCreationDate()
        );
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

    public Absence toEntity(CreateAbsenceDTO dto) {
        return new Absence(
                null,
                dto.absenceDate(),
                dto.justification()
        );
    }

    public AbsenceResponseDTO toAbsenceResponse(Absence entity) {
        UUID professionalId = entity.getGeneratedAppointment().getAppointment().getProfessionalId();
        UUID patientId = entity.getGeneratedAppointment().getPatientId();

        return new AbsenceResponseDTO(
                entity.getId(),
                entity.getGeneratedAppointment().getId(),
                patientId,
                professionalId,
                entity.getAbsenceDate(),
                entity.getJustification(),
                entity.getNotified()
        );
    }
}