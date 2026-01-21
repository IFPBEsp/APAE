package br.org.apae.api.professional.application.internal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.professional.application.mappers.AvailabilityMapper;
import br.org.apae.api.professional.domain.exceptions.AvailabilityConflictException;
import br.org.apae.api.professional.domain.exceptions.AvailabilityNotFoundException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.professional.domain.repository.AvailabilityRepository;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@Tag("service")
@Tag("availability")
@ExtendWith(MockitoExtension.class)
@DisplayName("AvailabilityApplicationServiceImpl - Testes de Unidade")
class AvailabilityApplicationServiceImplTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private HealthProfessionalRepository healthProfessionalRepository;

    @Mock
    private AvailabilityMapper availabilityMapper;

    @InjectMocks
    private AvailabilityApplicationServiceImpl availabilityService;

    private HealthProfessional mockProfessional;
    private Availability mockAvailability;
    private UUID professionalId;
    private UUID availabilityId;

    @BeforeEach
    void setUp() {
        professionalId = UUID.randomUUID();
        availabilityId = UUID.randomUUID();

        mockProfessional = new HealthProfessional(
                "João Silva",
                "joao@example.com",
                null,
                "(83) 98765-4321",
                "1234567",
                "CRM12345",
                null
        );
        mockProfessional.getId(); // Inicializar ID através de reflexão seria necessário, mas para testes mockaremos

        mockAvailability = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        // Usando reflexão para definir ID em teste seria necessário, mas mockaremos
    }

    @Test
    @DisplayName("Deve criar nova disponibilidade com sucesso")
    void shouldCreateAvailabilitySuccessfully() {
        // Arrange
        CreateAvailabilityDTO createDto = new CreateAvailabilityDTO("SEGUNDA", "MANHA");
        Availability availability = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        AvailabilityResponseDTO responseDTO = new AvailabilityResponseDTO(
                availabilityId, "SEGUNDA", "MANHA");

        when(healthProfessionalRepository.findById(professionalId))
                .thenReturn(Optional.of(mockProfessional));
        when(availabilityRepository.existsByProfessionalAndDayAndShift(
                any(HealthProfessional.class), eq(Day.SEGUNDA), eq(Shift.MANHA)))
                .thenReturn(false);
        when(availabilityMapper.toEntity(createDto, mockProfessional))
                .thenReturn(availability);
        when(availabilityRepository.save(any(Availability.class)))
                .thenReturn(availability);
        when(availabilityMapper.toResponseDTO(availability))
                .thenReturn(responseDTO);

        // Act
        AvailabilityResponseDTO result = availabilityService.createAvailability(professionalId, createDto);

        // Assert
        assertNotNull(result);
        assertEquals("SEGUNDA", result.day());
        assertEquals("MANHA", result.shift());
        verify(healthProfessionalRepository).findById(professionalId);
        verify(availabilityRepository).existsByProfessionalAndDayAndShift(
                mockProfessional, Day.SEGUNDA, Shift.MANHA);
        verify(availabilityRepository).save(any(Availability.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar disponibilidade com campos obrigatórios vazios")
    void shouldThrowExceptionWhenCreatingAvailabilityWithEmptyFields() {
        // Arrange
        CreateAvailabilityDTO createDtoEmptyDay = new CreateAvailabilityDTO("", "MANHA");
        CreateAvailabilityDTO createDtoEmptyShift = new CreateAvailabilityDTO("SEGUNDA", "");

        when(healthProfessionalRepository.findById(professionalId))
                .thenReturn(Optional.of(mockProfessional));

        // Act & Assert - Dia vazio
        assertThrows(IllegalArgumentException.class, () -> {
            availabilityService.createAvailability(professionalId, createDtoEmptyDay);
        });

        // Act & Assert - Turno vazio
        assertThrows(IllegalArgumentException.class, () -> {
            availabilityService.createAvailability(professionalId, createDtoEmptyShift);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar disponibilidade com profissional inexistente")
    void shouldThrowExceptionWhenProfessionalNotFound() {
        // Arrange
        CreateAvailabilityDTO createDto = new CreateAvailabilityDTO("SEGUNDA", "MANHA");

        when(healthProfessionalRepository.findById(professionalId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(HealthProfessionalNotFoundException.class, () -> {
            availabilityService.createAvailability(professionalId, createDto);
        });

        verify(healthProfessionalRepository).findById(professionalId);
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar conflitos de horários ao criar disponibilidade")
    void shouldValidateTimeConflictWhenCreatingAvailability() {
        // Arrange
        CreateAvailabilityDTO createDto = new CreateAvailabilityDTO("SEGUNDA", "MANHA");

        when(healthProfessionalRepository.findById(professionalId))
                .thenReturn(Optional.of(mockProfessional));
        when(availabilityRepository.existsByProfessionalAndDayAndShift(
                any(HealthProfessional.class), eq(Day.SEGUNDA), eq(Shift.MANHA)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(AvailabilityConflictException.class, () -> {
            availabilityService.createAvailability(professionalId, createDto);
        });

        verify(healthProfessionalRepository).findById(professionalId);
        verify(availabilityRepository).existsByProfessionalAndDayAndShift(
                mockProfessional, Day.SEGUNDA, Shift.MANHA);
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todas as disponibilidades registradas")
    void shouldListAllAvailabilities() {
        // Arrange
        Availability availability1 = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        Availability availability2 = new Availability(Day.TERCA, Shift.TARDE, mockProfessional);
        List<Availability> availabilities = Arrays.asList(availability1, availability2);

        AvailabilityResponseDTO response1 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "SEGUNDA", "MANHA");
        AvailabilityResponseDTO response2 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "TERCA", "TARDE");

        when(availabilityRepository.findAll()).thenReturn(availabilities);
        when(availabilityMapper.toResponseDTO(availability1)).thenReturn(response1);
        when(availabilityMapper.toResponseDTO(availability2)).thenReturn(response2);

        // Act
        List<AvailabilityResponseDTO> result = availabilityService.findAllAvailabilities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(availabilityRepository).findAll();
        verify(availabilityMapper, times(2)).toResponseDTO(any(Availability.class));
    }

    @Test
    @DisplayName("Deve retornar uma disponibilidade existente por ID")
    void shouldReturnExistingAvailabilityById() {
        // Arrange
        Availability availability = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        AvailabilityResponseDTO responseDTO = new AvailabilityResponseDTO(
                availabilityId, "SEGUNDA", "MANHA");

        when(availabilityRepository.findById(availabilityId))
                .thenReturn(Optional.of(availability));
        when(availabilityMapper.toResponseDTO(availability))
                .thenReturn(responseDTO);

        // Act
        AvailabilityResponseDTO result = availabilityService.findAvailabilityById(availabilityId);

        // Assert
        assertNotNull(result);
        assertEquals("SEGUNDA", result.day());
        assertEquals("MANHA", result.shift());
        verify(availabilityRepository).findById(availabilityId);
        verify(availabilityMapper).toResponseDTO(availability);
    }

    @Test
    @DisplayName("Deve retornar erro quando ID não existe")
    void shouldReturnErrorWhenIdDoesNotExist() {
        // Arrange
        when(availabilityRepository.findById(availabilityId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AvailabilityNotFoundException.class, () -> {
            availabilityService.findAvailabilityById(availabilityId);
        });

        verify(availabilityRepository).findById(availabilityId);
        verify(availabilityMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve atualizar horários de uma disponibilidade")
    void shouldUpdateAvailabilityTimes() {
        // Arrange
        Availability existingAvailability = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        UpdateAvailabilityDTO updateDto = new UpdateAvailabilityDTO("TERCA", "TARDE");
        AvailabilityResponseDTO responseDTO = new AvailabilityResponseDTO(
                availabilityId, "TERCA", "TARDE");

        when(availabilityRepository.findById(availabilityId))
                .thenReturn(Optional.of(existingAvailability));
        when(availabilityRepository.existsByProfessionalAndDayAndShift(
                any(HealthProfessional.class), eq(Day.TERCA), eq(Shift.TARDE)))
                .thenReturn(false);
        when(availabilityRepository.save(any(Availability.class)))
                .thenReturn(existingAvailability);
        when(availabilityMapper.toResponseDTO(existingAvailability))
                .thenReturn(responseDTO);

        // Act
        AvailabilityResponseDTO result = availabilityService.updateAvailability(availabilityId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("TERCA", result.day());
        assertEquals("TARDE", result.shift());
        verify(availabilityRepository).findById(availabilityId);
        verify(availabilityRepository).existsByProfessionalAndDayAndShift(
                mockProfessional, Day.TERCA, Shift.TARDE);
        verify(availabilityRepository).save(existingAvailability);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar disponibilidade inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentAvailability() {
        // Arrange
        UpdateAvailabilityDTO updateDto = new UpdateAvailabilityDTO("TERCA", "TARDE");

        when(availabilityRepository.findById(availabilityId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AvailabilityNotFoundException.class, () -> {
            availabilityService.updateAvailability(availabilityId, updateDto);
        });

        verify(availabilityRepository).findById(availabilityId);
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar conflito ao atualizar disponibilidade com horário já existente")
    void shouldValidateConflictWhenUpdatingToExistingTime() {
        // Arrange
        Availability existingAvailability = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        UpdateAvailabilityDTO updateDto = new UpdateAvailabilityDTO("TERCA", "TARDE");

        when(availabilityRepository.findById(availabilityId))
                .thenReturn(Optional.of(existingAvailability));
        when(availabilityRepository.existsByProfessionalAndDayAndShift(
                any(HealthProfessional.class), eq(Day.TERCA), eq(Shift.TARDE)))
                .thenReturn(true);

        // Act & Assert
        assertThrows(AvailabilityConflictException.class, () -> {
            availabilityService.updateAvailability(availabilityId, updateDto);
        });

        verify(availabilityRepository).findById(availabilityId);
        verify(availabilityRepository).existsByProfessionalAndDayAndShift(
                mockProfessional, Day.TERCA, Shift.TARDE);
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve excluir uma disponibilidade existente")
    void shouldDeleteExistingAvailability() {
        // Arrange
        when(availabilityRepository.existsById(availabilityId))
                .thenReturn(true);
        doNothing().when(availabilityRepository).deleteById(availabilityId);

        // Act
        assertDoesNotThrow(() -> {
            availabilityService.deleteAvailability(availabilityId);
        });

        // Assert
        verify(availabilityRepository).existsById(availabilityId);
        verify(availabilityRepository).deleteById(availabilityId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir disponibilidade inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentAvailability() {
        // Arrange
        when(availabilityRepository.existsById(availabilityId))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AvailabilityNotFoundException.class, () -> {
            availabilityService.deleteAvailability(availabilityId);
        });

        verify(availabilityRepository).existsById(availabilityId);
        verify(availabilityRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve listar disponibilidades de um profissional específico")
    void shouldListAvailabilitiesByProfessional() {
        // Arrange
        Availability availability1 = new Availability(Day.SEGUNDA, Shift.MANHA, mockProfessional);
        Availability availability2 = new Availability(Day.TERCA, Shift.TARDE, mockProfessional);
        List<Availability> availabilities = Arrays.asList(availability1, availability2);

        AvailabilityResponseDTO response1 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "SEGUNDA", "MANHA");
        AvailabilityResponseDTO response2 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "TERCA", "TARDE");

        when(healthProfessionalRepository.findById(professionalId))
                .thenReturn(Optional.of(mockProfessional));
        when(availabilityRepository.findByProfessional(mockProfessional))
                .thenReturn(availabilities);
        when(availabilityMapper.toResponseDTO(availability1)).thenReturn(response1);
        when(availabilityMapper.toResponseDTO(availability2)).thenReturn(response2);

        // Act
        List<AvailabilityResponseDTO> result = availabilityService.findAvailabilitiesByProfessional(professionalId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(healthProfessionalRepository).findById(professionalId);
        verify(availabilityRepository).findByProfessional(mockProfessional);
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar disponibilidades de profissional inexistente")
    void shouldThrowExceptionWhenProfessionalNotFoundForListing() {
        // Arrange
        when(healthProfessionalRepository.findById(professionalId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(HealthProfessionalNotFoundException.class, () -> {
            availabilityService.findAvailabilitiesByProfessional(professionalId);
        });

        verify(healthProfessionalRepository).findById(professionalId);
        verify(availabilityRepository, never()).findByProfessional(any());
    }
}
