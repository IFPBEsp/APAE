package br.org.apae.api.appointment.application.internal;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.domain.exceptions.AnnualRegistrationNotFound;
import br.org.apae.api.appointment.domain.exceptions.AppointmentAlreadyCancelledException;
import br.org.apae.api.appointment.domain.exceptions.AppointmentConflictException;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.exceptions.ProfessionalUnavailableException;
import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.appointment.domain.repository.AbsenceRepository;
import br.org.apae.api.appointment.domain.repository.AppointmentRepository;
import br.org.apae.api.appointment.domain.repository.GeneratedAppointmentRepository;
import br.org.apae.api.appointment.mapper.AppointmentMapper;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.TodayAppointmentsResponseDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.patient.application.mappers.ParentMapper;
import br.org.apae.api.patient.application.mappers.VaccineMapper;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Guardian;
import br.org.apae.api.patient.domain.model.Parent;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import br.org.apae.api.patient.domain.repository.GuardianRepository;
import br.org.apae.api.patient.domain.repository.ParentRepository;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AppointmentApplicationServiceImpl implements AppointmentApplicationService {

    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
    private final AppointmentRepository appointmentRepo;
    private final GeneratedAppointmentRepository generatedRepo;
    private final AnnualRegistryRepository registryRepo;
    private final HealthProfessionalRepository professionalRepo;
    private final AbsenceRepository absenceRepo;
    private final PatientRepository patientRepo;
    private final GuardianRepository guardianRepo;
    private final ParentRepository parentRepo;
    private final AppointmentMapper mapper;
    private final VaccineMapper vaccineMapper;
    private final ParentMapper parentMapper;

    public AppointmentApplicationServiceImpl(
            AppointmentRepository appointmentRepo,
            GeneratedAppointmentRepository generatedRepo,
            AnnualRegistryRepository registryRepo,
            HealthProfessionalRepository professionalRepo,
            AbsenceRepository absenceRepo,
            PatientRepository patientRepo,
            GuardianRepository guardianRepo,
            ParentRepository parentRepo,
            AppointmentMapper mapper) {
        this.appointmentRepo = appointmentRepo;
        this.generatedRepo = generatedRepo;
        this.registryRepo = registryRepo;
        this.professionalRepo = professionalRepo;
        this.absenceRepo = absenceRepo;
        this.patientRepo = patientRepo;
        this.guardianRepo = guardianRepo;
        this.parentRepo = parentRepo;
        this.mapper = mapper;
        this.vaccineMapper = new VaccineMapper();
        this.parentMapper = new ParentMapper();
    }

    @Override
    public void create(CreateAppointmentDTO dto) {
        List<Integer> validFrequencies = List.of(7, 14, 30);
        if (!validFrequencies.contains(dto.frequencyDays())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A frequência deve ser 7 (semanal), 14 (quinzenal) ou 30 (mensal).");
        }
        if (!dto.initialDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "A data do agendamento deve ser a partir de amanhã.");
        }
        
        AnnualRegistry annualRegistry = this.registryRepo
                .findByPatientIdAndYear(dto.patientId(), Year.now().getValue())
                .orElseThrow(AnnualRegistrationNotFound::new);

        HealthProfessional professional = this.professionalRepo
                .findById(dto.professionalId())
                .orElseThrow(HealthProfessionalNotFoundException::new);

        validateProfessionalAvailability(professional, dto.initialDate(), dto.hour());

        LocalTime exactTime = dto.hour().truncatedTo(ChronoUnit.MINUTES);

        LocalDate start = dto.initialDate();
        LocalDate end = start.plusYears(1);
        
        List<LocalDateTime> projectedDates = calculateRecurrence(start, dto.frequencyDays(), exactTime, start, end);

        validateNoDuplicateAppointments(dto.patientId(), dto.professionalId(), projectedDates, null);

        boolean isTimeSlotTaken = appointmentRepo.existsByProfessionalIdAndInitialDateAndHourAndIsActiveTrue(
                professional.getId(),
                dto.initialDate(),
                exactTime
        );
        
        if (isTimeSlotTaken) {
            throw new AppointmentConflictException();
        }
        
        Appointment appointment = mapper.toEntity(dto, professional, annualRegistry);
        
        try {
            appointmentRepo.save(appointment);
        } catch (DataIntegrityViolationException ex) {
            throw new AppointmentConflictException();
        }

        generateAppointments(appointment.getId(), start, end);
    }

    @Override
    public Page<AppointmentResponseDTO> findAll(Pageable pageable) {
        return this.appointmentRepo.findAll(pageable).map(appointment -> {
            Patient patient = patientRepo
                    .findById(appointment.getAnnualRegistration().getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Paciente não encontrado para o agendamento " + appointment.getId()
                    ));

            Guardian guardian = guardianRepo
                    .findByPatientId(patient.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Responsável não encontrado para o paciente " + patient.getId() + ", no agendamento " + appointment.getId()
                    ));
            List<Parent> pais = parentRepo.findAllByPatientId(patient.getId());

            AddressResponseDTO adto = new AddressResponseDTO(patient.getAddress());
            Set<Vaccine> vaccines = patient.getVaccines();

            PatientResponseDTO pdto = new PatientResponseDTO(
                    patient,
                    adto,
                    new GuardianResponseDTO(guardian, adto),
                    pais.stream().map(parentMapper::toResponseDTO).toList(),
                    vaccines.stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()), null);

            return mapper.toResponse(appointment, pdto);
        });
    }

    @Override
    public Page<AppointmentResponseDTO> findAllByDate(LocalDate date, Pageable pageable) {
        return this.appointmentRepo.findAllByInitialDate(date, pageable)
                .map(appointment -> {
                    Patient patient = patientRepo
                            .findById(appointment.getAnnualRegistration().getPatientId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Paciente não encontrado para o agendamento " + appointment.getId()
                            ));

                    Guardian guardian = guardianRepo
                            .findByPatientId(patient.getId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Responsável não encontrado para o paciente " + patient.getId() + ", no agendamento " + appointment.getId()
                            ));
                    List<Parent> pais = parentRepo.findAllByPatientId(patient.getId());

                    AddressResponseDTO adto = new AddressResponseDTO(patient.getAddress());
                    Set<Vaccine> vaccines = patient.getVaccines();

                    PatientResponseDTO pdto = new PatientResponseDTO(
                            patient,
                            adto,
                            new GuardianResponseDTO(guardian, adto),
                            pais.stream().map(parentMapper::toResponseDTO).collect(Collectors.toList()),
                            vaccines.stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()), null
                    );

                    return mapper.toResponse(appointment, pdto);
                });
    }

    @Override
    public Page<AppointmentResponseDTO> findAllByDateAndTime(LocalDate date, LocalTime time, Pageable pageable) {
        return this.appointmentRepo.findAllByInitialDateAndHour(date, time, pageable)
                .map(appointment -> {
                    Patient patient = patientRepo
                            .findById(appointment.getAnnualRegistration().getPatientId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Paciente não encontrado para o agendamento " + appointment.getId()
                            ));

                    Guardian guardian = guardianRepo
                            .findByPatientId(patient.getId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Responsável não encontrado para o paciente " + patient.getId() + ", no agendamento " + appointment.getId()
                            ));
                    List<Parent> pais = parentRepo.findAllByPatientId(patient.getId());

                    AddressResponseDTO adto = new AddressResponseDTO(patient.getAddress());
                    Set<Vaccine> vaccines = patient.getVaccines();

                    PatientResponseDTO pdto = new PatientResponseDTO(
                            patient,
                            adto,
                            new GuardianResponseDTO(guardian, adto),
                            pais.stream().map(parentMapper::toResponseDTO).collect(Collectors.toList()),
                            vaccines.stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()), null
                    );

                    return mapper.toResponse(appointment, pdto);
                });
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

        Patient patient = patientRepo
                .findById(appointment.getAnnualRegistration().getPatientId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Paciente não encontrado para o agendamento " + appointment.getId()
                ));

        Guardian guardian = guardianRepo
                .findByPatientId(patient.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Responsável não encontrado para o paciente " + patient.getId() + ", no agendamento " + appointment.getId()
                ));
        List<Parent> pais = parentRepo.findAllByPatientId(patient.getId());

        AddressResponseDTO adto = new AddressResponseDTO(patient.getAddress());
        Set<Vaccine> vaccines = patient.getVaccines();

        PatientResponseDTO pdto = new PatientResponseDTO(
                patient,
                adto,
                new GuardianResponseDTO(guardian, adto),
                pais.stream().map(parentMapper::toResponseDTO).collect(Collectors.toList()),
                vaccines.stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()), null);

        return mapper.toResponse(appointment, pdto);
    }

    @Override
    public void delete(UUID id) {
        if (!appointmentRepo.existsById(id)) {
            throw new AppointmentNotFoundException();
        }
        appointmentRepo.deleteById(id);
    }

    /**
     * Generates materialized appointment instances for an annual registration within a date range,
     * based on the active recurrence rule.
     * <p>
     * Existing generated appointments are reused; new ones are created and persisted.
     *
     * @param annualRegistrationId the ID of the annual patient registration
     * @param start the start date (inclusive) of the generation period
     * @param end the end date (inclusive) of the generation period
     * @return a list of generated appointment responses
     * @throws IllegalArgumentException if the annual registration is not found
     * @throws IllegalStateException if no active rule exists for the registration
     */

    public List<GeneratedAppointmentResponseDTO> generateAppointments(
            UUID appointmentId, LocalDate start, LocalDate end) {

        Appointment activeRule = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new IllegalStateException("Regra não encontrada"));

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

    @Override
    public Page<TodayAppointmentsResponseDTO> listAppointmentForToday(LocalDate date, Pageable pageable) {
        return this.generatedRepo.listAppointmentsForToday(date, pageable).map(appointment -> {
            try {
                Patient patient = patientRepo.findById(appointment.getPatientId())
                        .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
                
                Guardian guardian = guardianRepo.findByPatientId(patient.getId())
                        .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));
                    
                List<Parent> pais = parentRepo.findAllByPatientId(patient.getId());

                AddressResponseDTO adto = new AddressResponseDTO(patient.getAddress());
                Set<Vaccine> vaccines = patient.getVaccines();

                boolean hasAbsence = this.absenceRepo.findByGeneratedAppointmentId(appointment.getId()).isPresent();

                PatientResponseDTO pdto = new PatientResponseDTO(
                        patient,
                        adto,
                        new GuardianResponseDTO(guardian, adto),
                        pais.stream().map(parentMapper::toResponseDTO).toList(),
                        vaccines.stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()), null);

                return mapper.toTodayResponseDTO(appointment, pdto, hasAbsence);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar mapeamento de agendamento: " + e.getMessage(), e);
            }

        });
    }

    /**
     * Calculates recurring appointment dates based on a rule's start date, frequency, and time.
     *
     * @param ruleStart the start date of the recurrence rule
     * @param frequencyDays the interval in days between appointments
     * @param time the time of day for each appointment
     * @param queryStart the earliest date to include (inclusive)
     * @param queryEnd the latest date to include (inclusive)
     * @return a list of scheduled {@link LocalDateTime} instances
     */
    private List<LocalDateTime> calculateRecurrence(
            LocalDate ruleStart, int frequencyDays, LocalTime time,
            LocalDate queryStart, LocalDate queryEnd) {

        List<LocalDateTime> result = new ArrayList<>();
        LocalDate date = ruleStart.isBefore(queryStart) ? queryStart : ruleStart;

        while (!date.isAfter(queryEnd)) {
            result.add(date.atTime(time));

            if (frequencyDays == 30) {
                date = date.plusMonths(1);
            } else {
                date = date.plusDays(frequencyDays);
            }
        }
        return result;
    }

    /**
     * Updates an active appointment rule by deactivating the current rule and creating a new one
     * with updated data. Allows changing frequency, start date, time, and end date.
     * Removes future generated appointments and regenerates them based on the updated rule.
     *
     * @param appointmentId the ID of the active appointment rule to update
     * @param dto data to update the appointment (null fields keep current values)
     * @return the newly created active rule
     * @throws IllegalArgumentException if the rule is not found
     * @throws IllegalStateException if the rule is not active
     */
    public AppointmentResponseDTO update(UUID appointmentId, UpdateAppointmentDTO dto) {
        if (dto.frequencyDays() != null) {
            List<Integer> validFrequencies = List.of(7, 14, 30);
            if (!validFrequencies.contains(dto.frequencyDays())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A nova frequência deve ser 7 (semanal), 14 (quinzenal) ou 30 (mensal).");
            }
        }

        Appointment current = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Rule not found"));

        if (!current.isActive()) {
            throw new IllegalStateException("Only active rules can be edited");
        }

        LocalDate startDate = dto.initialDate() != null ? dto.initialDate() : current.getInitialDate();
        Integer frequencyDays = dto.frequencyDays() != null ? dto.frequencyDays() : current.getFrequencyDays();
        LocalTime appointmentHour = dto.hour() != null ? dto.hour() : current.getHour();

        validateProfessionalAvailability(current.getProfessional(), startDate, appointmentHour);

        LocalDate end = startDate.plusYears(1);
        List<LocalDateTime> projectedDates = calculateRecurrence(startDate, frequencyDays, appointmentHour, startDate, end);
        validateNoDuplicateAppointments(current.getAnnualRegistration().getPatientId(), current.getProfessional().getId(), projectedDates, current.getId());
        current.setActive(false);
        current.setEndDate(startDate.minusDays(1));
        appointmentRepo.save(current);

        generatedRepo.deleteFutureByAppointmentId(current.getId(), startDate.atStartOfDay());

        Appointment newRule = new Appointment(
                current.getProfessional(),
                current.getAnnualRegistration(),
                frequencyDays,
                appointmentHour,
                startDate,
                null
        );
        newRule = appointmentRepo.save(newRule);

        generateAppointments(newRule.getId(), startDate, end);

        Patient patient = patientRepo.findById(newRule.getAnnualRegistration().getPatientId())
                .orElseThrow();
        Guardian guardian = guardianRepo.findByPatientId(patient.getId())
                .orElseThrow();
        List<Parent> parents = parentRepo.findAllByPatientId(patient.getId());

        AddressResponseDTO addressDTO = new AddressResponseDTO(patient.getAddress());

        PatientResponseDTO patientDTO = new PatientResponseDTO(
                patient,
                addressDTO,
                new GuardianResponseDTO(guardian, addressDTO),
                parents.stream().map(parentMapper::toResponseDTO).toList(),
                patient.getVaccines().stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()),
                null
        );

        return mapper.toResponse(newRule, patientDTO);
    }

    /**
     * Reschedules a single generated appointment to a new date and time.
     *
     * @param generatedId the ID of the generated appointment
     * @param newDateTime the new scheduled date and time
     * @return the updated generated appointment response
     * @throws IllegalArgumentException if the appointment is not found
     */
    public GeneratedAppointmentResponseDTO reschedule(UUID generatedId, LocalDateTime newDateTime) {
        GeneratedAppointment appt = generatedRepo.findById(generatedId)
                .orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_NOT_FOUND));
        appt.setOverriddenDateTime(newDateTime);
        return mapper.toGeneratedResponse(generatedRepo.save(appt));
    }

    /**
     * Marks a generated appointment as performed/completed.
     *
     * @param generatedId the ID of the generated appointment
     * @return the updated generated appointment response
     * @throws IllegalArgumentException if the appointment is not found
     */
    public GeneratedAppointmentResponseDTO markAsPerformed(UUID generatedId) {
        GeneratedAppointment appt = generatedRepo.findById(generatedId)
                .orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_NOT_FOUND));
        appt.setPerformed(true);
        return mapper.toGeneratedResponse(generatedRepo.save(appt));
    }

    /**
     * Cancels a generated appointment with a reason.
     *
     * @param generatedId the ID of the generated appointment
     * @param reason the reason for cancellation (required)
     * @return the cancelled appointment response
     * @throws IllegalArgumentException if the appointment is not found
     */
    public GeneratedAppointmentResponseDTO cancel(UUID generatedId, String reason) {
        GeneratedAppointment appt = generatedRepo.findById(generatedId)
                .orElseThrow(() -> new IllegalArgumentException(APPOINTMENT_NOT_FOUND));
        if (Boolean.TRUE.equals(appt.getCancelled())) throw new AppointmentAlreadyCancelledException();
        appt.setCancelled(true);
        appt.setCancellationReason(reason);
        return mapper.toGeneratedResponse(generatedRepo.save(appt));
    }

    /**
     * Lists all generated appointments for a patient within a date range (paginated).
     *
     * @param patientId the ID of the patient (from annual registration)
     * @param start the start date of the range (inclusive)
     * @param end the end date of the range (inclusive)
     * @param pageable pagination information
     * @return a paginated list of generated appointments for the patient
     */
    public Page<GeneratedAppointmentResponseDTO> listByPatient(
            UUID patientId, LocalDate start, LocalDate end, Pageable pageable) {

        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(23, 59, 59);

        return generatedRepo.findByPatientIdAndScheduledDateTimeBetween(patientId, s, e, pageable)
                .map(mapper::toGeneratedResponse);
    }

    @Override
    public TodayAppointmentsResponseDTO findGeneratedAppointmentById(UUID id) {
        GeneratedAppointment appointment = generatedRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Generated appointment not found with id: " + id));

        Patient patient = patientRepo.findById(appointment.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Paciente não encontrado para o agendamento " + appointment.getId()
                ));

        Guardian guardian = guardianRepo.findByPatientId(patient.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Responsável não encontrado para o paciente " + patient.getId()
                ));

        List<Parent> pais = parentRepo.findAllByPatientId(patient.getId());

        AddressResponseDTO adto = new AddressResponseDTO(patient.getAddress());
        Set<Vaccine> vaccines = patient.getVaccines();

        Optional<Absence> absence = this.absenceRepo.findByGeneratedAppointmentId(id);
        Boolean hasAbsence = absence.isPresent();

        PatientResponseDTO pdto = new PatientResponseDTO(
                patient,
                adto,
                new GuardianResponseDTO(guardian, adto),
                pais.stream().map(parentMapper::toResponseDTO).toList(),
                vaccines.stream().map(vaccineMapper::toResponseDTO).collect(Collectors.toSet()),
                null
        );

        return mapper.toTodayResponseDTO(appointment, pdto, hasAbsence);
    }

    private void validateProfessionalAvailability(HealthProfessional professional, LocalDate date, LocalTime time) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new ProfessionalUnavailableException();
        }

        Day requestedDay = switch (dayOfWeek) {
            case MONDAY -> Day.SEGUNDA;
            case TUESDAY -> Day.TERCA;
            case WEDNESDAY -> Day.QUARTA;
            case THURSDAY -> Day.QUINTA;
            case FRIDAY -> Day.SEXTA;
            default -> throw new ProfessionalUnavailableException();
        };

        Shift requestedShift = time.getHour() < 12 ? Shift.MANHA : Shift.TARDE;

        boolean isAvailable = professional.getAvailabilities().stream()
                .anyMatch(availability ->
                        availability.getDay().equals(requestedDay) &&
                                availability.getShift().equals(requestedShift)
                );

        if (!isAvailable) {
            throw new ProfessionalUnavailableException();
        }
    }

    private void validateNoDuplicateAppointments(UUID patientId, UUID professionalId, List<LocalDateTime> generatedDates, UUID excludeAppointmentId) {
        for (LocalDateTime dt : generatedDates) {
            boolean conflict = generatedRepo.existsConflictForPatientAndProfessional(
                    patientId, professionalId, dt.toLocalDate(), excludeAppointmentId
            );
            
            if (conflict) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Este paciente já está agendado para este dia com este profissional.");
            }
        }
    }

}