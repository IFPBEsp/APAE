package br.org.apae.api.appointment.application.internal;

import br.org.apae.api.appointment.domain.exceptions.AppointmentNotFoundException;
import br.org.apae.api.appointment.domain.model.Absence;
import br.org.apae.api.appointment.domain.model.GeneratedAppointment;
import br.org.apae.api.appointment.domain.repository.AbsenceRepository;
import br.org.apae.api.appointment.domain.repository.GeneratedAppointmentRepository;
import br.org.apae.api.appointment.mapper.AbsenceMapper;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AbsenceApplicationServiceImplTest {

    @Mock
    private GeneratedAppointmentRepository generatedRepo;

    @Mock
    private AbsenceRepository absenceRepo;

    @Mock
    private AbsenceMapper absenceMapper;

    @InjectMocks
    private AbsenceApplicationServiceImpl service;

    private final UUID GEN_ID = UUID.randomUUID();
    private final UUID PATIENT_ID = UUID.randomUUID();
    private final LocalDate TODAY = LocalDate.now();
    private final LocalDateTime APPOINTMENT_DATE_TIME = LocalDateTime.of(TODAY, LocalTime.NOON);

    @BeforeEach
    void setup() {
    }

    @Test
    void registerShouldCreateAbsenceWhenValid() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);
        when(dto.absenceDate()).thenReturn(TODAY);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);
        when(generated.getEffectiveDateTime()).thenReturn(APPOINTMENT_DATE_TIME);

        Absence absenceEntity = mock(Absence.class);
        AbsenceResponseDTO responseDto = mock(AbsenceResponseDTO.class);

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(GEN_ID)).thenReturn(Optional.empty());
        when(absenceMapper.toEntity(dto)).thenReturn(absenceEntity);
        when(absenceRepo.save(absenceEntity)).thenReturn(absenceEntity);
        when(absenceMapper.toAbsenceResponse(absenceEntity)).thenReturn(responseDto);

        AbsenceResponseDTO result = service.register(dto);

        assertNotNull(result);
        assertSame(responseDto, result);

        verify(generated).setPerformed(false);
        verify(generated).setCancelled(false);
        verify(generatedRepo).save(generated);
        verify(absenceEntity).setGeneratedAppointment(generated);
        verify(absenceRepo).save(absenceEntity);
        verify(absenceMapper).toAbsenceResponse(absenceEntity);
    }

    @Test
    void registerShouldThrowExceptionWhenAppointmentNotFound() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);
        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> service.register(dto));

        verify(generatedRepo).findById(GEN_ID);
        verifyNoMoreInteractions(absenceRepo, absenceMapper);
    }

    @Test
    void registerShouldThrowExceptionWhenAbsenceAlreadyExists() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(GEN_ID)).thenReturn(Optional.of(mock(Absence.class)));

        assertThrows(IllegalStateException.class, () -> service.register(dto));

        verify(generatedRepo).findById(GEN_ID);
        verify(absenceRepo).findByGeneratedAppointmentId(GEN_ID);
        verifyNoMoreInteractions(absenceMapper, generatedRepo);
    }

    @Test
    void registerShouldThrowIllegalArgumentExceptionWhenAbsenceDateMismatchesAppointmentDate() {
        LocalDate appointmentDate = TODAY.plusDays(1);
        LocalDate absenceDate = TODAY;

        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);
        when(dto.absenceDate()).thenReturn(absenceDate);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);
        when(generated.getEffectiveDateTime()).thenReturn(LocalDateTime.of(appointmentDate, LocalTime.NOON));

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(GEN_ID)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.register(dto));

        verify(generatedRepo).findById(GEN_ID);
        verify(absenceRepo).findByGeneratedAppointmentId(GEN_ID);
        verify(generatedRepo, never()).save(any());
        verify(absenceRepo, never()).save(any());
        verifyNoMoreInteractions(absenceMapper);
    }

    @Test
    void registerShouldCreateAbsenceWhenJustificationIsNull() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);
        when(dto.absenceDate()).thenReturn(TODAY);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);
        when(generated.getEffectiveDateTime()).thenReturn(APPOINTMENT_DATE_TIME);

        Absence absenceEntity = mock(Absence.class);
        AbsenceResponseDTO responseDto = mock(AbsenceResponseDTO.class);

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(GEN_ID)).thenReturn(Optional.empty());
        when(absenceMapper.toEntity(dto)).thenReturn(absenceEntity);
        when(absenceRepo.save(absenceEntity)).thenReturn(absenceEntity);
        when(absenceMapper.toAbsenceResponse(absenceEntity)).thenReturn(responseDto);

        service.register(dto);

        verify(absenceRepo).save(absenceEntity);
        verify(absenceMapper).toEntity(dto);
    }

    @Test
    void registerShouldSetGeneratedAppointmentOnAbsenceEntity() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);
        when(dto.absenceDate()).thenReturn(TODAY);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);
        when(generated.getEffectiveDateTime()).thenReturn(APPOINTMENT_DATE_TIME);

        Absence absenceEntity = new Absence();

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(GEN_ID)).thenReturn(Optional.empty());
        when(absenceMapper.toEntity(dto)).thenReturn(absenceEntity);
        when(absenceRepo.save(any(Absence.class))).thenReturn(absenceEntity);
        when(absenceMapper.toAbsenceResponse(any(Absence.class))).thenReturn(mock(AbsenceResponseDTO.class));

        service.register(dto);

        assertSame(generated, absenceEntity.getGeneratedAppointment());
    }

    @Test
    void registerShouldResetGeneratedAppointmentStatusToNotPerformedAndNotCancelled() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);
        when(dto.absenceDate()).thenReturn(TODAY);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);
        when(generated.getEffectiveDateTime()).thenReturn(APPOINTMENT_DATE_TIME);

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(any())).thenReturn(Optional.empty());
        when(absenceMapper.toEntity(any())).thenReturn(mock(Absence.class));
        when(absenceRepo.save(any())).thenReturn(mock(Absence.class));
        when(absenceMapper.toAbsenceResponse(any())).thenReturn(mock(AbsenceResponseDTO.class));

        service.register(dto);

        verify(generated).setPerformed(false);
        verify(generated).setCancelled(false);
        verify(generatedRepo).save(generated);
    }

    @Test
    void registerShouldThrowExceptionWithCorrectMessageWhenAbsenceAlreadyExists() {
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(GEN_ID);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(GEN_ID);

        when(generatedRepo.findById(GEN_ID)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(GEN_ID)).thenReturn(Optional.of(mock(Absence.class)));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.register(dto));

        assertTrue(exception.getMessage().contains("Já existe uma falta registrada para o agendamento gerado de ID: " + GEN_ID));
    }

    @Test
    void findAllByFiltersShouldReturnMappedPage() {
        UUID generatedId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID professionalId = UUID.randomUUID();
        Pageable pageable = mock(Pageable.class);

        Page<Absence> rawPage = mock(Page.class);
        Page<AbsenceResponseDTO> mappedPage = mock(Page.class);

        when(absenceRepo.findByFilters(generatedId, patientId, professionalId, pageable)).thenReturn(rawPage);
        when(rawPage.map(ArgumentMatchers.<Function<Absence, AbsenceResponseDTO>>any())).thenReturn(mappedPage);

        Page<AbsenceResponseDTO> result = service.findAllByFilters(generatedId, patientId, professionalId, pageable);

        assertSame(mappedPage, result);

        verify(absenceRepo).findByFilters(generatedId, patientId, professionalId, pageable);
    }

    @Test
    void findAllByFiltersShouldHandleAllNullFilters() {
        Pageable pageable = mock(Pageable.class);
        Page<Absence> rawPage = mock(Page.class);
        when(rawPage.map(any())).thenReturn(mock(Page.class));

        when(absenceRepo.findByFilters(isNull(), isNull(), isNull(), eq(pageable))).thenReturn(rawPage);

        service.findAllByFilters(null, null, null, pageable);

        verify(absenceRepo).findByFilters(isNull(), isNull(), isNull(), eq(pageable));
    }

    @Test
    void findAllByFiltersShouldFilterByGeneratedIdOnly() {
        Pageable pageable = mock(Pageable.class);
        Page<Absence> rawPage = mock(Page.class);
        when(rawPage.map(any())).thenReturn(mock(Page.class));

        when(absenceRepo.findByFilters(GEN_ID, null, null, pageable)).thenReturn(rawPage);

        service.findAllByFilters(GEN_ID, null, null, pageable);

        verify(absenceRepo).findByFilters(eq(GEN_ID), isNull(), isNull(), eq(pageable));
    }

    @Test
    void findAllByFiltersShouldFilterByPatientIdOnly() {
        Pageable pageable = mock(Pageable.class);
        Page<Absence> rawPage = mock(Page.class);
        when(rawPage.map(any())).thenReturn(mock(Page.class));

        when(absenceRepo.findByFilters(null, PATIENT_ID, null, pageable)).thenReturn(rawPage);

        service.findAllByFilters(null, PATIENT_ID, null, pageable);

        verify(absenceRepo).findByFilters(isNull(), eq(PATIENT_ID), isNull(), eq(pageable));
    }

    @Test
    void findAllByFiltersShouldReturnEmptyPageWhenNoResultsFound() {
        Pageable pageable = mock(Pageable.class);
        Page<Absence> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(absenceRepo.findByFilters(any(), any(), any(), eq(pageable))).thenReturn(emptyPage);

        Page<AbsenceResponseDTO> result = service.findAllByFilters(GEN_ID, null, null, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(absenceMapper, never()).toAbsenceResponse(any());
    }

    @Test
    void findAllByFiltersShouldCallMapperFunctionForEachEntity() {
        Pageable pageable = mock(Pageable.class);
        Absence absence1 = mock(Absence.class);
        Absence absence2 = mock(Absence.class);
        List<Absence> absenceList = List.of(absence1, absence2);

        Page<Absence> rawPage = new PageImpl<>(absenceList, pageable, 2);

        when(absenceRepo.findByFilters(any(), any(), any(), eq(pageable))).thenReturn(rawPage);
        when(absenceMapper.toAbsenceResponse(any())).thenReturn(mock(AbsenceResponseDTO.class));

        Page<AbsenceResponseDTO> result = service.findAllByFilters(GEN_ID, null, null, pageable);

        verify(absenceMapper, times(2)).toAbsenceResponse(any(Absence.class));
        assertEquals(2, result.getTotalElements());
    }
}