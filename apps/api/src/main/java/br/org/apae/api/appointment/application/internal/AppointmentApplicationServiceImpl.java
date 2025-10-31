package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.domain.exceptions.AnnualRegistrationNotFound;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.*;
import br.org.apae.api.appointment.domain.repository.*;
import br.org.apae.api.appointment.mapper.AppointmentMapper;
import br.org.apae.api.common.dto.appointment.request.appointment.*;
import br.org.apae.api.common.dto.appointment.response.appointment.*;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@Transactional
public class AppointmentApplicationServiceImpl implements AppointmentApplicationService {

  public static final String APPOINTMENT_NOT_FOUND = "Appointment not found";
  private final AppointmentRepository appointmentRepo;
  private final GeneratedAppointmentRepository generatedRepo;
  private final AnnualRegistryRepository registryRepo;
  private final HealthProfessionalRepository professionalRepo;
  private final AbsenceRepository absenceRepo;
  private final AppointmentMapper mapper;


  public AppointmentApplicationServiceImpl(
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
  public void create(CreateAppointmentDTO dto) {
    Appointment appointment = mapper.toEntity(dto);

    AnnualRegistry annualRegistry = this.registryRepo.findById(dto.annualRegistration())
        .orElseThrow(AnnualRegistrationNotFound::new);

    appointment.setAnnualRegistration(annualRegistry);
    appointmentRepo.save(appointment);
    generateAppointments(annualRegistry.getId(), appointment.getInitialDate(), appointment.getEndDate());
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
  public AppointmentResponseDTO update(UUID id, UpdateAppointmentDTO dto) {
    Appointment appointment = appointmentRepo.findById(id)
            .orElseThrow(AppointmentNotFoundException::new);

    Appointment toUpdate = mapper.updateEntity(appointment, dto);

    if (dto.annualRegistrationId() != null) {
      AnnualRegistry annualRegistry = this.registryRepo.findById(dto.annualRegistrationId())
          .orElseThrow(AnnualRegistrationNotFound::new);

      appointment.setAnnualRegistration(annualRegistry);
    }
    Appointment updated = appointmentRepo.save(toUpdate);
    return mapper.toResponse(updated);
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
      date = date.plusDays(frequencyDays);
    }
    return result;
  }

  /**
   * Updates an active appointment rule by deactivating the current rule and creating a new one
   * with updated frequency and/or time. Generates new future appointments and removes outdated ones.
   * <p>
   * This creates a historical trail of rule changes.
   *
   * @param ruleId the ID of the active rule to update
   * @param newFrequency optional new frequency in days (null to keep current)
   * @param newTime optional new appointment time (null to keep current)
   * @return the newly created active rule
   * @throws IllegalArgumentException if the rule is not found
   * @throws IllegalStateException if the rule is not active
   */
  public AppointmentResponseDTO updateRule(UUID ruleId, Integer newFrequency, LocalTime newTime) {
    Appointment current = appointmentRepo.findById(ruleId)
            .orElseThrow(() -> new IllegalArgumentException("Rule not found"));

    if (!current.isActive()) {
      throw new IllegalStateException("Only active rules can be edited");
    }

    LocalDate editDate = LocalDate.now();

    // Deactivate old rule
    current.setActive(false);
    current.setEndDate(editDate.minusDays(1));
    appointmentRepo.save(current);

    // Create new rule
    Appointment newRule = new Appointment(
            current.getProfessionalId(),
            current.getServiceId(),
            current.getAnnualRegistration(),
            newFrequency != null ? newFrequency : current.getFrequencyDays(),
            newTime != null ? newTime : current.getHour(),
            editDate,
            null // end date will be set on next edit
    );
    newRule = appointmentRepo.save(newRule);

    // Regenerate future appointments and clean up old ones
    generateAppointments(current.getAnnualRegistration().getId(), editDate, editDate.plusYears(1));
    generatedRepo.deleteFutureByAppointmentId(current.getId(), editDate.atStartOfDay());

    return mapper.toResponse(newRule);
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
}