package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.domain.model.Appointment;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.appointment.domain.repository.AppointmentRepository;
import br.org.apae.api.appointment.domain.repository.GeneratedAppointmentRepository;
import br.org.apae.api.appointment.mapper.AppointmentMapper;
import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.GeneratedAppointmentResponseDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.TodayAppointmentsResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentApplicationServiceImplTest {

  @Mock
  AppointmentRepository appointmentRepo;

  @Mock
  AnnualRegistryRepository annualRegistryRepo;

  @Mock
  HealthProfessionalRepository professionalRepos;

  @Mock
  AppointmentMapper appointmentMapper;

  @Mock
  GeneratedAppointmentRepository generatedAppointmentRepo;

  @InjectMocks
  AppointmentApplicationServiceImpl appointmentService;

  @Mock
  Appointment appointment;
  @Mock
  AnnualRegistry annualRegistry;
  @Mock
  HealthProfessional healthProfessional;
  @Mock
  CreateAppointmentDTO createAppointmentDTO;
  @Mock
  GeneratedAppointment generatedAppointment;
  @Mock
  AppointmentResponseDTO appointmentResponseDTO;
  @Mock
  GeneratedAppointmentResponseDTO generatedAppointmentResponseDTO;


  @Test
  @DisplayName("Deve criar um agendamento com sucesso")
  void shouldCreateAppointmentSuccessfully() {
    UUID appointmentId = UUID.randomUUID();
    UUID annualRegistryId = UUID.randomUUID();

    LocalDate initialDate = LocalDate.now().minusDays(10);
    LocalTime hour = LocalTime.of(10, 0);
    int frequencyDays = 7;
    Year registryYear = Year.now();

    when(appointment.getId()).thenReturn(appointmentId);
    when(appointment.getInitialDate()).thenReturn(initialDate);
    when(appointment.getFrequencyDays()).thenReturn(frequencyDays);
    when(appointment.getHour()).thenReturn(hour);

    when(annualRegistry.getId()).thenReturn(annualRegistryId);
    when(annualRegistry.getYear()).thenReturn(registryYear);

    when(annualRegistryRepo.findByPatientIdAndYear(createAppointmentDTO.patientId(), Year.now()))
        .thenReturn(Optional.of(annualRegistry));

    when(professionalRepos.findById(createAppointmentDTO.professionalId()))
        .thenReturn(Optional.of(healthProfessional));

    when(appointmentMapper.toEntity(createAppointmentDTO, healthProfessional, annualRegistry))
        .thenReturn(appointment);

    when(appointmentRepo.save(appointment))
        .thenReturn(appointment);

    when(appointmentRepo.findByAnnualRegistrationIdAndIsActiveTrue(annualRegistryId))
        .thenReturn(List.of(appointment));

    when(generatedAppointmentRepo.findByAppointmentIdAndScheduledDateTime(any(), any()))
        .thenReturn(Optional.of(generatedAppointment));

    when(appointmentMapper.toGeneratedResponse(any()))
        .thenReturn(mock(GeneratedAppointmentResponseDTO.class));

    appointmentService.create(createAppointmentDTO);

    verify(annualRegistryRepo).findByPatientIdAndYear(createAppointmentDTO.patientId(), Year.now());
    verify(professionalRepos).findById(createAppointmentDTO.professionalId());
    verify(appointmentMapper).toEntity(createAppointmentDTO, healthProfessional, annualRegistry);
    verify(appointmentRepo).save(appointment);

    verify(appointmentRepo).findByAnnualRegistrationIdAndIsActiveTrue(annualRegistryId);
    verify(generatedAppointmentRepo, atLeastOnce())
        .findByAppointmentIdAndScheduledDateTime(eq(appointmentId), any(LocalDateTime.class));
    verify(appointmentMapper, atLeastOnce())
        .toGeneratedResponse(any());
  }

  @Test
  @DisplayName("Deve gerar os agendamentos com sucesso")
  void shouldGeneratedAppointmentSuccessfully() {
    UUID aptId = UUID.randomUUID();
    UUID annualId = UUID.randomUUID();
    LocalDate initialDate = LocalDate.now().minusDays(10);
    int freqDays = 7;
    LocalTime hour = LocalTime.of(10, 0);

    when(appointment.getId()).thenReturn(aptId);
    when(appointment.getInitialDate()).thenReturn(initialDate);
    when(appointment.getFrequencyDays()).thenReturn(freqDays);
    when(appointment.getHour()).thenReturn(hour);

    when(appointmentRepo.findByAnnualRegistrationIdAndIsActiveTrue(annualId))
        .thenReturn(List.of(appointment));

    when(generatedAppointmentRepo.findByAppointmentIdAndScheduledDateTime(any(), any()))
        .thenReturn(Optional.empty());

    when(appointmentMapper.toGeneratedResponse(any()))
        .thenReturn(mock(GeneratedAppointmentResponseDTO.class));

    appointmentService.generateAppointments(
        annualId,
        LocalDate.now(),
        LocalDate.now().plusDays(5));

    verify(appointmentRepo).findByAnnualRegistrationIdAndIsActiveTrue(any());
    verify(generatedAppointmentRepo, atLeastOnce()).findByAppointmentIdAndScheduledDateTime(any(), any());
    verify(generatedAppointmentRepo, atLeastOnce()).save(any());
    verify(appointmentMapper, atLeastOnce()).toGeneratedResponse(any());
  }

  @Test
  @DisplayName("Deve buscar todos os agendamentos com sucesso")
  void shouldFindAllAppointmentsSuccessfully() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Appointment> pageOfAppointments = new PageImpl<>(List.of(appointment), pageable, 1);

    when(appointmentRepo.findAll(pageable))
        .thenReturn(pageOfAppointments);

    when(appointmentMapper.toResponse(appointment)).thenReturn(appointmentResponseDTO);

    Page<AppointmentResponseDTO> result = appointmentService.findAll(pageable);

    verify(appointmentRepo).findAll(pageable);
    verify(appointmentMapper).toResponse(appointment);

    assertEquals(1, result.getTotalElements());
    assertEquals(List.of(appointmentResponseDTO), result.getContent());
  }

  @Test
  @DisplayName("Deve buscar todos os agendamentos pela data")
  void shouldFindAllAppointmentsByDateSuccessfully() {
    LocalDate date = LocalDate.now();
    Pageable pageable = PageRequest.of(0, 10);

    Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment));

    when(appointmentRepo.findAllByInitialDate(date, pageable))
        .thenReturn(appointmentPage);

    when(appointmentMapper.toResponse(appointment))
        .thenReturn(appointmentResponseDTO);

    Page<AppointmentResponseDTO> result =
        appointmentService.findAllByDate(date, pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(appointmentResponseDTO, result.getContent().getFirst());

    verify(appointmentRepo, times(1))
        .findAllByInitialDate(date, pageable);

    verify(appointmentMapper, times(1))
        .toResponse(appointment);

    verifyNoMoreInteractions(appointmentRepo, appointmentMapper);
  }

  @Test
  @DisplayName("Deve buscar todos os agendamentos pela data e hora")
  void shouldFindAllAppointmentsByDateAndTimeSuccessfully() {
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.of(10, 0);
    Pageable pageable = PageRequest.of(0, 10);

    Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment));

    when(appointmentRepo.findAllByInitialDateAndHour(date, time, pageable))
        .thenReturn(appointmentPage);

    when(appointmentMapper.toResponse(appointment))
        .thenReturn(appointmentResponseDTO);

    Page<AppointmentResponseDTO> result = appointmentService.findAllByDateAndTime(date, time, pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(appointmentResponseDTO, result.getContent().getFirst());

    verify(appointmentRepo, times(1))
        .findAllByInitialDateAndHour(date, time, pageable);

    verify(appointmentMapper, times(1))
        .toResponse(appointment);

    verifyNoMoreInteractions(appointmentRepo, appointmentMapper);
  }


  @Test
  @DisplayName("Deve buscar um agendamento pelo id")
  void shouldFindByIdSuccessFully() {
    UUID id = UUID.randomUUID();
    when(appointmentRepo.findById(id))
        .thenReturn(Optional.of(appointment));

    when(appointmentMapper.toResponse(appointment))
        .thenReturn(appointmentResponseDTO);

    AppointmentResponseDTO result = appointmentService.findById(id);

    assertNotNull(result);
    assertEquals(appointmentResponseDTO, result);

    verify(appointmentRepo, times(1))
        .findById(id);

    verify(appointmentMapper, times(1))
        .toResponse(appointment);

    verifyNoMoreInteractions(appointmentRepo, appointmentMapper);
  }

  @Test
  @DisplayName("Deve deletar um agendamento pelo id")
  void shouldDeleteAppointmentSuccessFully() {
    UUID id = UUID.randomUUID();

    when(appointmentRepo.existsById(id)).thenReturn(true);

    appointmentService.delete(id);

    verify(appointmentRepo, times(1)).existsById(id);
    verify(appointmentRepo, times(1)).deleteById(id);
    verifyNoMoreInteractions(appointmentRepo, appointmentMapper);
  }

  @Test
  @DisplayName("Deve listar todos os agendamentos do dia com sucesso")
  void shouldListAppointmentsForTodaySuccessfully() {
    Pageable pageable = PageRequest.of(0, 10);

    GeneratedAppointment generated = mock(GeneratedAppointment.class);
    TodayAppointmentsResponseDTO responseDTO = mock(TodayAppointmentsResponseDTO.class);

    Page<GeneratedAppointment> repoPage =
        new PageImpl<>(List.of(generated));

    when(generatedAppointmentRepo.listAppointmentsForToday(pageable))
        .thenReturn(repoPage);

    when(appointmentMapper.toTodayResponseDTO(generated))
        .thenReturn(responseDTO);

    Page<TodayAppointmentsResponseDTO> result =
        appointmentService.listAppointmentForToday(pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(responseDTO, result.getContent().getFirst());

    verify(generatedAppointmentRepo, times(1))
        .listAppointmentsForToday(pageable);

    verify(appointmentMapper, times(1))
        .toTodayResponseDTO(generated);

    verifyNoMoreInteractions(generatedAppointmentRepo, appointmentMapper);
  }

  @Test
  @DisplayName("Deve atualizar um agendamento com sucesso")
  void shouldUpdateAppointmentSuccessfully() {
    UUID aptId = UUID.randomUUID();
    UUID annualId = UUID.randomUUID();
    UUID newRuleId = UUID.randomUUID();

    Year year = Year.of(2025);

    when(appointment.isActive())
        .thenReturn(true);
    when(appointment.getId())
        .thenReturn(aptId);
    when(appointment.getProfessional())
        .thenReturn(healthProfessional);
    when(appointment.getServiceId())
        .thenReturn(UUID.randomUUID());
    when(appointment.getAnnualRegistration())
        .thenReturn(annualRegistry);

    when(annualRegistry.getId())
        .thenReturn(annualId);

    when(annualRegistry.getYear())
        .thenReturn(year);

    when(appointmentRepo.findById(aptId))
        .thenReturn(Optional.of(appointment));

    Appointment savedNewRule = mock(Appointment.class);
    when(appointmentRepo.save(any(Appointment.class)))
        .thenReturn(savedNewRule);

    when(appointmentMapper.toResponse(savedNewRule))
        .thenReturn(appointmentResponseDTO);

    when(appointmentRepo.findByAnnualRegistrationIdAndIsActiveTrue(annualId))
        .thenReturn(List.of(savedNewRule));

    when(savedNewRule.getInitialDate())
        .thenReturn(LocalDate.now());

    when(savedNewRule.getFrequencyDays())
        .thenReturn(12);

    when(savedNewRule.getHour())
        .thenReturn(LocalTime.now());

    when(savedNewRule.getId())
        .thenReturn(newRuleId);

    when(generatedAppointmentRepo.findByAppointmentIdAndScheduledDateTime(any(), any()))
        .thenReturn(Optional.empty());

    when(appointmentMapper.toGeneratedResponse(any()))
        .thenReturn(mock(GeneratedAppointmentResponseDTO.class));

    AppointmentResponseDTO result = appointmentService.updateAppointment(
        aptId,
        5,
        LocalTime.of(14, 0)
    );

    verify(appointmentRepo).findById(aptId);
    verify(appointment).setActive(false);
    verify(appointment).setEndDate(LocalDate.now().minusDays(1));
    verify(appointmentRepo).save(appointment);
    verify(appointmentRepo, atLeastOnce()).save(any(Appointment.class));
    verify(appointmentMapper).toResponse(savedNewRule);
    assertEquals(appointmentResponseDTO, result);
  }

  @Test
  @DisplayName("Deve reagendar um agendamento com sucesso")
  void shouldRescheduleGeneratedAppointmentSuccessfully() {
    UUID id = UUID.randomUUID();
    LocalDateTime newDate = LocalDateTime.of(2025, 1, 10, 14, 30);

    when(generatedAppointmentRepo.findById(id))
        .thenReturn(Optional.of(generatedAppointment));

    when(generatedAppointmentRepo.save(generatedAppointment))
        .thenReturn(generatedAppointment);

    when(appointmentMapper.toGeneratedResponse(generatedAppointment))
        .thenReturn(generatedAppointmentResponseDTO);

    GeneratedAppointmentResponseDTO result = appointmentService.reschedule(id, newDate);

    verify(generatedAppointment).setOverriddenDateTime(newDate);
    verify(generatedAppointmentRepo).save(generatedAppointment);
    verify(appointmentMapper).toGeneratedResponse(generatedAppointment);
    assertEquals(generatedAppointmentResponseDTO, result);
  }

  @Test
  @DisplayName("Deve marca com realizado um agendamento com sucesso")
  void shouldMarkAsPerformedGeneratedAppointmentSuccessfully() {
    UUID id = UUID.randomUUID();

    when(generatedAppointmentRepo.findById(id))
        .thenReturn(Optional.of(generatedAppointment));

    when(generatedAppointmentRepo.save(generatedAppointment))
        .thenReturn(generatedAppointment);

    when(appointmentMapper.toGeneratedResponse(generatedAppointment))
        .thenReturn(generatedAppointmentResponseDTO);

    GeneratedAppointmentResponseDTO result = appointmentService.markAsPerformed(id);

    verify(generatedAppointment).setPerformed(true);
    verify(generatedAppointmentRepo).save(generatedAppointment);
    verify(appointmentMapper).toGeneratedResponse(generatedAppointment);
    assertEquals(generatedAppointmentResponseDTO, result);
  }


  @Test
  @DisplayName("Deve cancelar um agendamento com sucesso")
  void shouldCancelGeneratedAppointmentSuccessfully() {
    UUID id = UUID.randomUUID();
    String reason = "reason";

    when(generatedAppointmentRepo.findById(id))
        .thenReturn(Optional.of(generatedAppointment));

    when(generatedAppointmentRepo.save(generatedAppointment))
        .thenReturn(generatedAppointment);

    when(appointmentMapper.toGeneratedResponse(generatedAppointment))
        .thenReturn(generatedAppointmentResponseDTO);

    GeneratedAppointmentResponseDTO result = appointmentService.cancel(id, reason);

    verify(generatedAppointment).setCancelled(true);
    verify(generatedAppointment).setCancellationReason(reason);
    verify(generatedAppointmentRepo).save(generatedAppointment);
    verify(appointmentMapper).toGeneratedResponse(generatedAppointment);
    assertEquals(generatedAppointmentResponseDTO, result);
  }

  @Test
  @DisplayName("Deve listar os agendamentos de um paciente com sucesso")
  void shouldListGeneratedAppointmentsByPatientSuccessfully() {
    UUID patientId = UUID.randomUUID();
    LocalDate start = LocalDate.now();
    LocalDate end = LocalDate.now();
    Pageable pageable = PageRequest.of(0, 10);
    Page<GeneratedAppointment> pageOfAppointments = new PageImpl<>(List.of(generatedAppointment), pageable, 1);
    Page<GeneratedAppointmentResponseDTO> pageOfAppointmentsDto = new PageImpl<>(List.of(generatedAppointmentResponseDTO), pageable, 1);

    LocalDateTime s = start.atStartOfDay();
    LocalDateTime e = end.atTime(23, 59, 59);

    when(generatedAppointmentRepo.findByPatientIdAndScheduledDateTimeBetween(patientId, s, e, pageable))
        .thenReturn(pageOfAppointments);

    when(appointmentMapper.toGeneratedResponse(generatedAppointment))
        .thenReturn(generatedAppointmentResponseDTO);

    Page<GeneratedAppointmentResponseDTO> result = appointmentService.listByPatient(patientId, start, end, pageable);

    verify(generatedAppointmentRepo).findByPatientIdAndScheduledDateTimeBetween(patientId, s, e, pageable);
    assertEquals(pageOfAppointmentsDto, result);
  }

}