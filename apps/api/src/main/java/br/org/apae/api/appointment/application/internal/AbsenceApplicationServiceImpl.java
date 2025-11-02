package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.appointment.domain.exceptions.AbsenceNotFoundException;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.appointment.domain.repository.AbsenceRepository;
import br.org.apae.api.appointment.domain.repository.GeneratedAppointmentRepository;
import br.org.apae.api.appointment.mapper.AppointmentMapper;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class AbsenceApplicationServiceImpl implements AbsenceApplicationService {

    private final AbsenceRepository absenceRepo;
    private final GeneratedAppointmentRepository generatedRepo;
    private final AppointmentMapper mapper;

    public AbsenceApplicationServiceImpl(
            AbsenceRepository absenceRepo,
            GeneratedAppointmentRepository generatedRepo,
            AppointmentMapper mapper) {
        this.absenceRepo = absenceRepo;
        this.generatedRepo = generatedRepo;
        this.mapper = mapper;
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

        Absence absence = mapper.toEntity(dto);
        absence.setGeneratedAppointment(generatedAppointment);

        if (!generatedAppointment.getPerformed()) {
            generatedAppointment.setPerformed(false);
            generatedRepo.save(generatedAppointment);
        }

        absence = absenceRepo.save(absence);
        return mapper.toAbsenceResponse(absence);
    }

    @Override
    public Page<AbsenceResponseDTO> findAllByFilters(
            UUID generatedId, UUID patientId, UUID professionalId, Pageable pageable) {

        return absenceRepo.findByFilters(generatedId, patientId, professionalId, pageable)
                .map(mapper::toAbsenceResponse);
    }
}