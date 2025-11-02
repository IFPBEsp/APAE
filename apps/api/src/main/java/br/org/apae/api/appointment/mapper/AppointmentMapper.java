package br.org.apae.api.appointment.mapper;

import br.org.apae.api.appointment.domain.model.Absence; // <- NOVO IMPORT
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO; // <- NOVO IMPORT
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO; // <- NOVO IMPORT
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppointmentMapper {

    // --- Mapeamentos de Appointment (Regras de Recorrência) ---

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

    // --- Mapeamento de GeneratedAppointment (Agendamento Gerado) ---

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

    // --- Mapeamento de Absence (Falta) ---

    /**
     * Converte um DTO de criação de falta para a entidade Absence.
     * O GeneratedAppointment é setado no Service, não aqui.
     */
    public Absence toEntity(CreateAbsenceDTO dto) {
        return new Absence(
                null, // GeneratedAppointment será setado no Service
                dto.absenceDate(),
                dto.justification()
        );
    }

    /**
     * Converte a entidade Absence para o DTO de resposta.
     * Extrai o ID do Paciente e do Profissional do GeneratedAppointment.
     */
    public AbsenceResponseDTO toAbsenceResponse(Absence entity) {
        // Garante que o GeneratedAppointment foi carregado no service antes de chamar
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