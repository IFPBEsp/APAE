package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.*;
import br.org.apae.api.appointment.domain.repository.*;
import br.org.apae.api.appointment.mapper.AbsenceMapper;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.common.dto.appointment.response.absence.JustifyAbsenceDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@Primary
public class AbsenceApplicationServiceImpl implements AbsenceApplicationService {

    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    private final GeneratedAppointmentRepository generatedRepo;
    private final AbsenceRepository absenceRepo;
    private final AbsenceMapper absenceMapper;

    public AbsenceApplicationServiceImpl(
            GeneratedAppointmentRepository generatedRepo,
            AbsenceRepository absenceRepo,
            AbsenceMapper absenceMapper) {
        this.generatedRepo = generatedRepo;
        this.absenceRepo = absenceRepo;
        this.absenceMapper = absenceMapper;
    }

    @Override
    public AbsenceResponseDTO register(CreateAbsenceDTO dto) {
        GeneratedAppointment generatedAppointment = generatedRepo.findById(dto.generatedAppointmentId())
                .orElseThrow(AppointmentNotFoundException::new);

        if (absenceRepo.findByGeneratedAppointmentId(generatedAppointment.getId()).isPresent()) {
            throw new IllegalStateException("Já existe uma falta registrada para o agendamento gerado de ID: " + generatedAppointment.getId());
        }

        if (!dto.absenceDate().isEqual(generatedAppointment.getEffectiveDateTime().toLocalDate())) {
            throw new IllegalArgumentException("A data da falta deve ser igual à data efetiva do agendamento gerado: " + generatedAppointment.getEffectiveDateTime().toLocalDate());
        }

        Absence absence = absenceMapper.toEntity(dto);
        absence.setGeneratedAppointment(generatedAppointment);

        generatedAppointment.setPerformed(false);
        generatedAppointment.setCancelled(false);
        generatedRepo.save(generatedAppointment);

        absence = absenceRepo.save(absence);
        return absenceMapper.toAbsenceResponse(absence);
    }

    @Override
    public Page<AbsenceResponseDTO> findAllByFilters(
            UUID generatedId, UUID patientId, UUID professionalId, Pageable pageable) {

        return absenceRepo.findByFilters(generatedId, patientId, professionalId, pageable)
                .map(absenceMapper::toAbsenceResponse);
    }

    @Override
    public AbsenceResponseDTO justify(UUID absenceId, JustifyAbsenceDTO dto) {
        Absence absence = absenceRepo.findById(absenceId).orElseThrow(() -> new EntityNotFoundException("Falta não encontrada"));

        absence.setIsJustified(true);
        absence.setJustification(dto.justification());
    
        absence = absenceRepo.save(absence);
        return absenceMapper.toAbsenceResponse(absence);
    }

}