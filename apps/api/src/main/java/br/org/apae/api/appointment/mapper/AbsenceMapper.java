package br.org.apae.api.appointment.mapper;

import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AbsenceMapper {

    public Absence toEntity(CreateAbsenceDTO dto) {
        return new Absence(
                null,
                dto.absenceDate(),
                dto.justification()
        );
    }

    public AbsenceResponseDTO toAbsenceResponse(Absence entity) {
        UUID professionalId = entity.getGeneratedAppointment().getAppointment().getProfessional().getId();
        UUID patientId = entity.getGeneratedAppointment().getPatientId();

        return new AbsenceResponseDTO(
                entity.getId(),
                entity.getGeneratedAppointment().getId(),
                patientId,
                professionalId,
                entity.getAbsenceDate(),
                entity.getJustification(),
                entity.getNotified(),
                entity.getIsJustified(),
                entity.getJustificationDocumentId()
        );
    }
}