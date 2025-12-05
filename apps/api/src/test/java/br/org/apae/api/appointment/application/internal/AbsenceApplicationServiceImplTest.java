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
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    @BeforeEach
    void setup() {
    }

    @Test
    void registerShouldCreateAbsenceWhenValid() {
        UUID genId = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(genId);
        when(dto.absenceDate()).thenReturn(today);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(genId);
        when(generated.getEffectiveDateTime()).thenReturn(LocalDateTime.of(today, java.time.LocalTime.NOON));

        Absence absenceEntity = mock(Absence.class);
        AbsenceResponseDTO responseDto = mock(AbsenceResponseDTO.class);

        when(generatedRepo.findById(genId)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(genId)).thenReturn(Optional.empty());
        when(absenceMapper.toEntity(dto)).thenReturn(absenceEntity);
        when(absenceRepo.save(absenceEntity)).thenReturn(absenceEntity);
        when(absenceMapper.toAbsenceResponse(absenceEntity)).thenReturn(responseDto);

        AbsenceResponseDTO result = service.register(dto);

        assertNotNull(result);
        assertSame(responseDto, result);

        verify(generatedRepo).findById(genId);
        verify(absenceRepo).findByGeneratedAppointmentId(genId);
        verify(absenceMapper).toEntity(dto);
        verify(generated).setPerformed(false);
        verify(generated).setCancelled(false);
        verify(generatedRepo).save(generated);
        verify(absenceRepo).save(absenceEntity);
        verify(absenceMapper).toAbsenceResponse(absenceEntity);
    }

    @Test
    void registerShouldThrowExceptionWhenAppointmentNotFound() {
        UUID genId = UUID.randomUUID();
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(genId);

        when(generatedRepo.findById(genId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> service.register(dto));

        verify(generatedRepo).findById(genId);
        verifyNoMoreInteractions(absenceRepo, absenceMapper);
    }

    @Test
    void registerShouldThrowExceptionWhenAbsenceAlreadyExists() {
        UUID genId = UUID.randomUUID();
        
        CreateAbsenceDTO dto = mock(CreateAbsenceDTO.class);
        when(dto.generatedAppointmentId()).thenReturn(genId);

        GeneratedAppointment generated = mock(GeneratedAppointment.class);
        when(generated.getId()).thenReturn(genId);

        when(generatedRepo.findById(genId)).thenReturn(Optional.of(generated));
        when(absenceRepo.findByGeneratedAppointmentId(genId))
            .thenReturn(Optional.of(mock(Absence.class)));

        assertThrows(IllegalStateException.class, () -> service.register(dto));

        verify(generatedRepo).findById(genId);
        verify(absenceRepo).findByGeneratedAppointmentId(genId);
        verifyNoMoreInteractions(absenceMapper);
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

        when(rawPage.map(Mockito.<java.util.function.Function<Absence, AbsenceResponseDTO>>any())).thenReturn(mappedPage);

        Page<AbsenceResponseDTO> result = service.findAllByFilters(generatedId, patientId, professionalId, pageable);

        assertSame(mappedPage, result);

        verify(absenceRepo).findByFilters(generatedId, patientId, professionalId, pageable);
        verify(rawPage).map(Mockito.<java.util.function.Function<Absence, AbsenceResponseDTO>>any());
    }
}
