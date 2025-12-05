package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.domain.exceptions.AnnualRegistrationNotFound;
import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.repository.AppointmentRepository;
import br.org.apae.api.appointment.domain.repository.GeneratedAppointmentRepository;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentApplicationServiceImplExceptionTest {

  @Mock
  AnnualRegistryRepository annualRegistryRepo;
  @Mock
  AppointmentRepository appointmentRepo;
  @InjectMocks
  AppointmentApplicationServiceImpl appointmentService;
  @Mock
  CreateAppointmentDTO createAppointmentDTO;
  @Mock
  AnnualRegistry annualRegistry;
  @Mock
  Appointment appointment;
  @Mock
  GeneratedAppointmentRepository generatedAppointmentRepo;
  @Mock
  HealthProfessionalRepository healthProfessionalRepo;

  @Test
  @DisplayName("Deve lançar AnnualRegistrationNotFound ao criar um agendamento")
  void shouldThrownAnnualRegistrationNotFoundOnCreate() {
    UUID patientId = UUID.randomUUID();

    when(createAppointmentDTO.patientId())
        .thenReturn(patientId);

    when(annualRegistryRepo.findByPatientIdAndYear(patientId, Year.now()))
        .thenReturn(Optional.empty());

    assertThrows(AnnualRegistrationNotFound.class,
        () -> appointmentService.create(createAppointmentDTO));
  }

  @Test
  @DisplayName("Deve lançar HealthProfessionalNotFound ao criar agendamento sem profissional vinculado")
  void shouldThrownHealthProfessionalNotFoundOnCreate() {
    UUID patientId = UUID.randomUUID();
    UUID professionalId = UUID.randomUUID();

    when(createAppointmentDTO.patientId())
        .thenReturn(patientId);

    when(createAppointmentDTO.professionalId())
        .thenReturn(professionalId);

    when(annualRegistryRepo.findByPatientIdAndYear(patientId, Year.now()))
        .thenReturn(Optional.of(annualRegistry));

    when(healthProfessionalRepo.findById(professionalId))
        .thenReturn(Optional.empty());

    assertThrows(HealthProfessionalNotFoundException.class,
        () -> appointmentService.create(createAppointmentDTO));
  }

  @Test
  @DisplayName("Deve lançar IllegalStateException ao gerar agendamentos sem regras ativas")
  void shouldThrownActiveRuleNotFoundOnGenerateAppointments() {
    UUID annualId = UUID.randomUUID();
    LocalDate start = LocalDate.now();
    LocalDate end = LocalDate.now();

    when(appointmentRepo.findByAnnualRegistrationIdAndIsActiveTrue(annualId))
        .thenReturn(Collections.emptyList());

    assertThrows(IllegalStateException.class,
        () -> appointmentService.generateAppointments(annualId, start, end));
  }

  @Test
  @DisplayName("Deve lançar IllegalArgumentException ao atualizar agendamento inexistente")
  void shouldThrownRuleNotFoundOnUpdateAppointment() {
    UUID aptId = UUID.randomUUID();
    Integer newFrequency = 12;
    LocalTime newTime = LocalTime.now();

    when(appointmentRepo.findById(aptId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> appointmentService.updateAppointment(aptId, newFrequency, newTime));
  }

  @Test
  @DisplayName("Deve lançar IllegalStateException ao tentar editar uma regra inativa")
  void shouldThrownOnlyActiveRulesCanBeEditedOnUpdateAppointment() {
    UUID aptId = UUID.randomUUID();
    Integer newFrequency = 12;
    LocalTime newTime = LocalTime.now();

    when(appointmentRepo.findById(aptId)).thenReturn(Optional.of(appointment));
    when(appointment.isActive()).thenReturn(Boolean.FALSE);

    assertThrows(IllegalStateException.class,
        () -> appointmentService.updateAppointment(aptId, newFrequency, newTime));
  }

  @Test
  @DisplayName("Deve lançar IllegalArgumentException ao reagendar agendamento inexistente")
  void shouldThrownNotFoundOnRescheduleAppointment() {
    UUID aptId = UUID.randomUUID();
    LocalDateTime newDateTime = LocalDateTime.now();

    when(generatedAppointmentRepo.findById(aptId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> appointmentService.reschedule(aptId, newDateTime));
  }

  @Test
  @DisplayName("Deve lançar IllegalArgumentException ao marcar como realizado um agendamento inexistente")
  void shouldThrownNotFoundOnMarkAsPerformedAppointment() {
    UUID aptId = UUID.randomUUID();

    when(generatedAppointmentRepo.findById(aptId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> appointmentService.markAsPerformed(aptId));
  }

  @Test
  @DisplayName("Deve lançar IllegalArgumentException ao cancelar agendamento inexistente")
  void shouldThrownNotFoundOnCancelAppointment() {
    UUID aptId = UUID.randomUUID();
    String reason = "reason";

    when(generatedAppointmentRepo.findById(aptId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> appointmentService.cancel(aptId, reason));
  }

  @Test
  @DisplayName("Deve lançar AppointmentNotFoundException quando o agendamento não existir ao deletar")
  void shouldThrownAppointmentNotFoundOnDelete(){
    UUID aptId = UUID.randomUUID();

    when(appointmentRepo.existsById(aptId))
        .thenReturn(Boolean.FALSE);

    assertThrows(AppointmentNotFoundException.class,
        () -> appointmentService.delete(aptId));
  }
}
