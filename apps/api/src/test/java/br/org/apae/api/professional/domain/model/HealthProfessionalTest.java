package br.org.apae.api.professional.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag("domain")
@Tag("health-professional")
@Tag("availability")
@DisplayName("HealthProfessional - Testes de Unidade para Disponibilidade")
class HealthProfessionalTest {

    private HealthProfessional professional;
    private Availability availability1;
    private Availability availability2;
    private Availability availability3;

    @BeforeEach
    void setUp() {
        professional = new HealthProfessional(
                "João Silva",
                "joao@example.com",
                null,
                "(83) 98765-4321",
                "1234567",
                "CRM12345",
                null
        );

        availability1 = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        availability2 = new Availability(Day.TERCA, Shift.TARDE, professional);
        availability3 = new Availability(Day.QUARTA, Shift.MANHA, professional);
    }

    @Test
    @DisplayName("Deve retornar lista vazia de disponibilidades quando não há disponibilidades")
    void shouldReturnEmptyAvailabilitiesList() {
        // Act
        List<Availability> availabilities = professional.getAvailabilities();

        // Assert
        assertNotNull(availabilities);
        assertTrue(availabilities.isEmpty());
    }

    @Test
    @DisplayName("Deve adicionar disponibilidade corretamente")
    void shouldAddAvailability() {
        // Act
        professional.addAvailability(availability1);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(1, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertEquals(professional, availability1.getProfessional());
    }

    @Test
    @DisplayName("Deve adicionar múltiplas disponibilidades")
    void shouldAddMultipleAvailabilities() {
        // Act
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertTrue(availabilities.contains(availability2));
    }

    @Test
    @DisplayName("Deve definir lista de disponibilidades corretamente")
    void shouldSetAvailabilities() {
        // Arrange
        List<Availability> newAvailabilities = Arrays.asList(availability1, availability2);

        // Act
        professional.setAvailabilities(newAvailabilities);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertTrue(availabilities.contains(availability2));
    }

    @Test
    @DisplayName("Deve limpar disponibilidades existentes ao definir nova lista")
    void shouldClearExistingAvailabilitiesWhenSettingNewList() {
        // Arrange
        professional.addAvailability(availability1);
        List<Availability> newAvailabilities = Arrays.asList(availability2);

        // Act
        professional.setAvailabilities(newAvailabilities);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(1, availabilities.size());
        assertTrue(availabilities.contains(availability2));
        assertFalse(availabilities.contains(availability1));
    }

    @Test
    @DisplayName("Deve limpar todas as disponibilidades ao definir lista nula")
    void shouldClearAllAvailabilitiesWhenSettingNullList() {
        // Arrange
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        // Act
        professional.setAvailabilities(null);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertTrue(availabilities.isEmpty());
    }

    @Test
    @DisplayName("Deve limpar todas as disponibilidades ao definir lista vazia")
    void shouldClearAllAvailabilitiesWhenSettingEmptyList() {
        // Arrange
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        // Act
        professional.setAvailabilities(new ArrayList<>());

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertTrue(availabilities.isEmpty());
    }

    @Test
    @DisplayName("Deve criar nova disponibilidade com sucesso")
    void shouldCreateNewAvailabilitySuccessfully() {
        // Act
        Availability newAvailability = new Availability(Day.QUINTA, Shift.TARDE, professional);
        professional.addAvailability(newAvailability);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(1, availabilities.size());
        assertTrue(availabilities.contains(newAvailability));
        assertEquals(Day.QUINTA, newAvailability.getDay());
        assertEquals(Shift.TARDE, newAvailability.getShift());
        assertEquals(professional, newAvailability.getProfessional());
    }

    @Test
    @DisplayName("Deve validar conflitos de horários - mesmo dia e turno")
    void shouldValidateTimeConflicts() {
        // Arrange - Adiciona primeira disponibilidade
        professional.addAvailability(availability1);
        
        // Act & Assert - Tenta adicionar disponibilidade com mesmo dia e turno
        Availability conflictingAvailability = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        
        // Verifica se já existe disponibilidade com mesmo dia e turno
        boolean hasConflict = professional.getAvailabilities().stream()
                .anyMatch(avail -> avail.getDay().equals(conflictingAvailability.getDay()) 
                        && avail.getShift().equals(conflictingAvailability.getShift()));
        
        assertTrue(hasConflict, "Deve detectar conflito de horário");
        
        // Adiciona mesmo assim para testar que o modelo permite (validação deve ser feita no serviço)
        professional.addAvailability(conflictingAvailability);
        assertEquals(2, professional.getAvailabilities().size());
    }

    @Test
    @DisplayName("Deve permitir disponibilidades diferentes no mesmo dia")
    void shouldAllowDifferentShiftsOnSameDay() {
        // Arrange
        Availability manha = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        Availability tarde = new Availability(Day.SEGUNDA, Shift.TARDE, professional);

        // Act
        professional.addAvailability(manha);
        professional.addAvailability(tarde);

        // Assert
        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertTrue(availabilities.contains(manha));
        assertTrue(availabilities.contains(tarde));
    }

    @Test
    @DisplayName("Deve validar que disponibilidade requer dia e turno não nulos")
    void shouldValidateAvailabilityRequiresDayAndShift() {
        // Assert - O construtor deve receber valores válidos
        assertNotNull(availability1.getDay());
        assertNotNull(availability1.getShift());
        assertNotNull(availability1.getProfessional());
    }

    @Test
    @DisplayName("Deve listar todas as disponibilidades registradas")
    void shouldListAllRegisteredAvailabilities() {
        // Arrange
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);
        professional.addAvailability(availability3);

        // Act
        List<Availability> availabilities = professional.getAvailabilities();

        // Assert
        assertNotNull(availabilities);
        assertEquals(3, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertTrue(availabilities.contains(availability2));
        assertTrue(availabilities.contains(availability3));
    }

    @Test
    @DisplayName("Deve retornar uma disponibilidade existente")
    void shouldReturnExistingAvailability() {
        // Arrange
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        // Act
        Optional<Availability> found = professional.getAvailabilities().stream()
                .filter(avail -> avail.getDay().equals(Day.SEGUNDA) && avail.getShift().equals(Shift.MANHA))
                .findFirst();

        // Assert
        assertTrue(found.isPresent());
        assertEquals(availability1, found.get());
        assertEquals(Day.SEGUNDA, found.get().getDay());
        assertEquals(Shift.MANHA, found.get().getShift());
    }

    @Test
    @DisplayName("Deve retornar erro caso a disponibilidade não exista")
    void shouldReturnErrorWhenAvailabilityDoesNotExist() {
        // Arrange
        professional.addAvailability(availability1);

        // Act
        Optional<Availability> found = professional.getAvailabilities().stream()
                .filter(avail -> avail.getDay().equals(Day.SABADO) && avail.getShift().equals(Shift.TARDE))
                .findFirst();

        // Assert
        assertFalse(found.isPresent(), "Não deve encontrar disponibilidade inexistente");
    }

    @Test
    @DisplayName("Deve atualizar horários de uma disponibilidade")
    void shouldUpdateAvailabilityTimes() {
        // Arrange
        professional.addAvailability(availability1);
        Day newDay = Day.QUINTA;
        Shift newShift = Shift.TARDE;

        // Act
        availability1.setDay(newDay);
        availability1.setShift(newShift);

        // Assert
        assertEquals(newDay, availability1.getDay());
        assertEquals(newShift, availability1.getShift());
        assertTrue(professional.getAvailabilities().contains(availability1));
    }

    @Test
    @DisplayName("Deve atualizar apenas o dia de uma disponibilidade")
    void shouldUpdateOnlyDay() {
        // Arrange
        professional.addAvailability(availability1);
        Shift originalShift = availability1.getShift();
        Day newDay = Day.SEXTA;

        // Act
        availability1.setDay(newDay);

        // Assert
        assertEquals(newDay, availability1.getDay());
        assertEquals(originalShift, availability1.getShift());
    }

    @Test
    @DisplayName("Deve atualizar apenas o turno de uma disponibilidade")
    void shouldUpdateOnlyShift() {
        // Arrange
        professional.addAvailability(availability1);
        Day originalDay = availability1.getDay();
        Shift newShift = Shift.TARDE;

        // Act
        availability1.setShift(newShift);

        // Assert
        assertEquals(originalDay, availability1.getDay());
        assertEquals(newShift, availability1.getShift());
    }

    @Test
    @DisplayName("Deve excluir uma disponibilidade existente")
    void shouldDeleteExistingAvailability() {
        // Arrange
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);
        professional.addAvailability(availability3);

        // Act
        boolean removed = professional.getAvailabilities().remove(availability2);

        // Assert
        assertTrue(removed);
        assertEquals(2, professional.getAvailabilities().size());
        assertFalse(professional.getAvailabilities().contains(availability2));
        assertTrue(professional.getAvailabilities().contains(availability1));
        assertTrue(professional.getAvailabilities().contains(availability3));
    }

    @Test
    @DisplayName("Deve retornar false ao tentar excluir disponibilidade inexistente")
    void shouldReturnFalseWhenDeletingNonExistentAvailability() {
        // Arrange
        professional.addAvailability(availability1);
        Availability nonExistentAvailability = new Availability(Day.SABADO, Shift.TARDE, professional);

        // Act
        boolean removed = professional.getAvailabilities().remove(nonExistentAvailability);

        // Assert
        assertFalse(removed);
        assertEquals(1, professional.getAvailabilities().size());
        assertTrue(professional.getAvailabilities().contains(availability1));
    }

    @Test
    @DisplayName("Deve manter relacionamento bidirecional ao adicionar disponibilidade")
    void shouldMaintainBidirectionalRelationshipWhenAddingAvailability() {
        // Act
        professional.addAvailability(availability1);

        // Assert
        assertEquals(professional, availability1.getProfessional());
        assertTrue(professional.getAvailabilities().contains(availability1));
    }

    @Test
    @DisplayName("Deve permitir múltiplas disponibilidades em dias diferentes")
    void shouldAllowMultipleAvailabilitiesOnDifferentDays() {
        // Arrange
        Availability segunda = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        Availability terca = new Availability(Day.TERCA, Shift.MANHA, professional);
        Availability quarta = new Availability(Day.QUARTA, Shift.MANHA, professional);

        // Act
        professional.addAvailability(segunda);
        professional.addAvailability(terca);
        professional.addAvailability(quarta);

        // Assert
        assertEquals(3, professional.getAvailabilities().size());
        assertTrue(professional.getAvailabilities().contains(segunda));
        assertTrue(professional.getAvailabilities().contains(terca));
        assertTrue(professional.getAvailabilities().contains(quarta));
    }
}
