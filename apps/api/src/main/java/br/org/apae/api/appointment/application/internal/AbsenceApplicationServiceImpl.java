package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.domain.exceptions.AnnualRegistrationNotFound;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.*;
import br.org.apae.api.appointment.domain.repository.*;
import br.org.apae.api.appointment.mapper.AppointmentMapper;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.*;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.*;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@Transactional
@Primary
public class AbsenceApplicationServiceImpl implements AbsenceApplicationService, AppointmentApplicationService {

    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    private final AppointmentRepository appointmentRepo;
    private final GeneratedAppointmentRepository generatedRepo;
    private final AnnualRegistryRepository registryRepo;
    private final HealthProfessionalRepository professionalRepo;
    private final AbsenceRepository absenceRepo;
    private final AppointmentMapper mapper;


    public AbsenceApplicationServiceImpl(
            AppointmentRepository appointmentRepo,
            GeneratedAppointmentRepository generatedRepo,
            AnnualRegistryRepository registryRepo,
            HealthProfessionalRepository professionalRepo,
            AbsenceRepository absenceRepo,
            AppointmentMapper mapper) {
        this.appointmentRepo = appointmentRepo;
        this.generatedRepo = generatedRepo;
        this.registryRepo = registryRepo;
        this.professionalRepo = professionalRepo;
        this.absenceRepo = absenceRepo;
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

        generatedAppointment.setPerformed(false);
        generatedAppointment.setCancelled(false);
        generatedRepo.save(generatedAppointment);

        absence = absenceRepo.save(absence);
        return mapper.toAbsenceResponse(absence);
    }

    @Override
    public Page<AbsenceResponseDTO> findAllByFilters(
            UUID generatedId, UUID patientId, UUID professionalId, Pageable pageable) {

        return absenceRepo.findByFilters(generatedId, patientId, professionalId, pageable)
                .map(mapper::toAbsenceResponse);
    }

    @Override
    public void create(CreateAppointmentDTO dto) {
        Appointment appointment = mapper.toEntity(dto);

        AnnualRegistry annualRegistry = this.registryRepo.findById(dto.annualRegistration())
                .orElseThrow(AnnualRegistrationNotFound::new);

        appointment.setAnnualRegistration(annualRegistry);
        appointmentRepo.save(appointment);

        int year = annualRegistry.getYear().getValue();
        LocalDate end = LocalDate.of(year, 12, 31);
        generateAppointments(annualRegistry.getId(), appointment.getInitialDate(), end);
    }

    @Override
    public Page<AppointmentResponseDTO> findAll(Pageable pageable) {
        return appointmentRepo.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<AppointmentResponseDTO> findAllByDate(LocalDate date, Pageable pageable) {
        return appointmentRepo.findAllByInitialDate(date, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<AppointmentResponseDTO> findAllByDateAndTime(LocalDate date, LocalTime time, Pageable pageable) {
        return appointmentRepo.findAllByInitialDateAndHour(date, time, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<AppointmentResponseDTO> findAll(LocalDate date, LocalTime time, Pageable pageable) {
        if (date != null && time == null) {
            return findAllByDate(date, pageable);
        } else if (date != null) {
            return findAllByDateAndTime(date, time, pageable);
        }
        return findAll(pageable);
    }

    @Override
    public AppointmentResponseDTO findById(UUID id) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(AppointmentNotFoundException::new);
        return mapper.toResponse(appointment);
    }

    @Override
    public void delete(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            throw new AppointmentNotFoundException();
        }
        appointmentRepo.deleteById(id);
    }

    public List<GeneratedAppointmentResponseDTO> generateAppointments(
            UUID annualRegistrationId, LocalDate start, LocalDate end) {

        Appointment activeRule = appointmentRepo.findByAnnualRegistrationIdAndIsActiveTrue(annualRegistrationId)
                .stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No active rule found"));

        List<LocalDateTime> dates = calculateRecurrence(
                activeRule.getInitialDate(),
                activeRule.getFrequencyDays(),
                activeRule.getHour(),
                start, end);

        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt = end.atTime(23, 59, 59);

        List<GeneratedAppointment> generated = new ArrayList<>();
        for (LocalDateTime dt : dates) {
            if (dt.isBefore(startDt) || dt.isAfter(endDt)) continue;

            GeneratedAppointment existing = generatedRepo
                    .findByAppointmentIdAndScheduledDateTime(activeRule.getId(), dt)
                    .orElse(null);

            if (existing == null) {
                existing = new GeneratedAppointment(activeRule, dt);
                generatedRepo.save(existing);
            }
            generated.add(existing);
        }

        return generated.stream()
                .map(mapper::toGeneratedResponse)
                .toList();
    }

    private List<LocalDateTime> calculateRecurrence(
            LocalDate ruleStart, int frequencyDays, LocalTime time,
            LocalDate queryStart, LocalDate queryEnd) {

        List<LocalDateTime> result = new ArrayList<>();
        LocalDate date = ruleStart.isBefore(queryStart) ? queryStart : ruleStart;

        while (!date.isAfter(queryEnd)) {
            result.add(date.atTime(time));
            date = date.plusDays(frequencyDays);
        }
        return result;
    }

    public AppointmentResponseDTO updateAppointment(UUID appointmentId, Integer newFrequency, LocalTime newTime) {
        Appointment current = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found"));

        if (!current.isActive()) {
            throw new IllegalStateException("Only active rules can be edited");
        }

        LocalDate editDate = LocalDate.now();

        current.setActive(false);
        current.setEndDate(editDate.minusDays(1));
        appointmentRepo.save(current);

        Appointment newRule = new Appointment(
                current.getProfessionalId(),
                current.getServiceId(),
                current.getAnnualRegistration(),
                newFrequency != null ? newFrequency : current.getFrequencyDays(),
                newTime != null ? newTime : current.getHour(),
                editDate,
                null
        );
        newRule = appointmentRepo.save(newRule);

        int year = current.getAnnualRegistration().getYear().getValue();
        LocalDate end = LocalDate.of(year, 12, 31);

        generateAppointments(current.getAnnualRegistration().getId(), editDate, end);
        generatedRepo.deleteFutureByAppointmentId(current.getId(), editDate.atStartOfDay());

        return mapper.toResponse(newRule);
    }

    public GeneratedAppointmentResponseDTO reschedule(UUID generatedId, LocalDateTime newDateTime) {
        GeneratedAppointment appt = generatedRepo.findById(generatedId)
                .orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_NOT_FOUND));
        appt.setOverriddenDateTime(newDateTime);
        return mapper.toGeneratedResponse(generatedRepo.save(appt));
    }

    public GeneratedAppointmentResponseDTO markAsPerformed(UUID generatedId) {
        GeneratedAppointment appt = generatedRepo.findById(generatedId)
                .orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_NOT_FOUND));
        appt.setPerformed(true);
        return mapper.toGeneratedResponse(generatedRepo.save(appt));
    }

    public GeneratedAppointmentResponseDTO cancel(UUID generatedId, String reason) {
        GeneratedAppointment appt = generatedRepo.findById(generatedId)
                .orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_NOT_FOUND));
        appt.setCancelled(true);
        appt.setCancellationReason(reason);
        return mapper.toGeneratedResponse(generatedRepo.save(appt));
    }

    public Page<GeneratedAppointmentResponseDTO> listByPatient(
            UUID patientId, LocalDate start, LocalDate end, Pageable pageable) {

        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        return generatedRepo.findByPatientIdAndScheduledDateTimeBetween(patientId, s, e, pageable)
                .map(mapper::toGeneratedResponse);
    }
}