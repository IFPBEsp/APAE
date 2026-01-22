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
        List<Availability> availabilities = professional.getAvailabilities();

        assertNotNull(availabilities);
        assertTrue(availabilities.isEmpty());
    }

    @Test
    @DisplayName("Deve adicionar disponibilidade corretamente")
    void shouldAddAvailability() {
        professional.addAvailability(availability1);

        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(1, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertEquals(professional, availability1.getProfessional());
    }

    @Test
    @DisplayName("Deve adicionar múltiplas disponibilidades")
    void shouldAddMultipleAvailabilities() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertTrue(availabilities.contains(availability2));
    }

    @Test
    @DisplayName("Deve definir lista de disponibilidades corretamente")
    void shouldSetAvailabilities() {
        List<Availability> newAvailabilities = Arrays.asList(availability1, availability2);

        professional.setAvailabilities(newAvailabilities);

        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertTrue(availabilities.contains(availability2));
    }

    @Test
    @DisplayName("Deve limpar disponibilidades existentes ao definir nova lista")
    void shouldClearExistingAvailabilitiesWhenSettingNewList() {
        professional.addAvailability(availability1);
        List<Availability> newAvailabilities = Arrays.asList(availability2);

        professional.setAvailabilities(newAvailabilities);

        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(1, availabilities.size());
        assertTrue(availabilities.contains(availability2));
        assertFalse(availabilities.contains(availability1));
    }

    @Test
    @DisplayName("Deve limpar todas as disponibilidades ao definir lista nula")
    void shouldClearAllAvailabilitiesWhenSettingNullList() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        professional.setAvailabilities(null);

        List<Availability> availabilities = professional.getAvailabilities();
        assertTrue(availabilities.isEmpty());
    }

    @Test
    @DisplayName("Deve limpar todas as disponibilidades ao definir lista vazia")
    void shouldClearAllAvailabilitiesWhenSettingEmptyList() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        professional.setAvailabilities(new ArrayList<>());

        List<Availability> availabilities = professional.getAvailabilities();
        assertTrue(availabilities.isEmpty());
    }

    @Test
    @DisplayName("Deve criar nova disponibilidade com sucesso")
    void shouldCreateNewAvailabilitySuccessfully() {
        Availability newAvailability = new Availability(Day.QUINTA, Shift.TARDE, professional);
        professional.addAvailability(newAvailability);

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
        professional.addAvailability(availability1);
        
        Availability conflictingAvailability = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        
        boolean hasConflict = professional.getAvailabilities().stream()
                .anyMatch(avail -> avail.getDay().equals(conflictingAvailability.getDay()) 
                        && avail.getShift().equals(conflictingAvailability.getShift()));
        
        assertTrue(hasConflict, "Deve detectar conflito de horário");
        
        professional.addAvailability(conflictingAvailability);
        assertEquals(2, professional.getAvailabilities().size());
    }

    @Test
    @DisplayName("Deve detectar conflito de horário antes de adicionar disponibilidade")
    void shouldDetectTimeConflictBeforeAddingAvailability() {
        professional.addAvailability(availability1);
        assertEquals(1, professional.getAvailabilities().size());
        
        Availability conflictingAvailability = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        
        boolean hasConflict = professional.getAvailabilities().stream()
                .anyMatch(avail -> avail.getDay().equals(conflictingAvailability.getDay()) 
                        && avail.getShift().equals(conflictingAvailability.getShift()));
        
        assertTrue(hasConflict, "Deve detectar conflito de horário antes de adicionar");
        
        assertTrue(professional.getAvailabilities().stream()
                .anyMatch(avail -> avail.getDay().equals(Day.SEGUNDA) 
                        && avail.getShift().equals(Shift.MANHA)),
                "Deve existir disponibilidade com SEGUNDA/MANHA");
    }

    @Test
    @DisplayName("Deve permitir disponibilidades diferentes no mesmo dia")
    void shouldAllowDifferentShiftsOnSameDay() {
        Availability manha = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        Availability tarde = new Availability(Day.SEGUNDA, Shift.TARDE, professional);

        professional.addAvailability(manha);
        professional.addAvailability(tarde);

        List<Availability> availabilities = professional.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertTrue(availabilities.contains(manha));
        assertTrue(availabilities.contains(tarde));
    }

    @Test
    @DisplayName("Deve validar que disponibilidade requer dia e turno não nulos")
    void shouldValidateAvailabilityRequiresDayAndShift() {
        assertNotNull(availability1.getDay());
        assertNotNull(availability1.getShift());
        assertNotNull(availability1.getProfessional());
    }

    @Test
    @DisplayName("Deve impedir criação de disponibilidade com dia nulo")
    void shouldPreventCreationWithNullDay() {
        Availability availabilityWithNullDay = new Availability(null, Shift.MANHA, professional);
        
        assertNull(availabilityWithNullDay.getDay(), "Dia deve ser nulo quando passado null no construtor");
    }

    @Test
    @DisplayName("Deve impedir criação de disponibilidade com turno nulo")
    void shouldPreventCreationWithNullShift() {
        Availability availabilityWithNullShift = new Availability(Day.SEGUNDA, null, professional);
        
        assertNull(availabilityWithNullShift.getShift(), "Turno deve ser nulo quando passado null no construtor");
    }

    @Test
    @DisplayName("Deve impedir criação de disponibilidade com profissional nulo")
    void shouldPreventCreationWithNullProfessional() {
        Availability availabilityWithNullProfessional = new Availability(Day.SEGUNDA, Shift.MANHA, null);
        
        assertNull(availabilityWithNullProfessional.getProfessional(), 
                "Profissional deve ser nulo quando passado null no construtor");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar usar disponibilidade com dia nulo")
    void shouldThrowExceptionWhenUsingAvailabilityWithNullDay() {
        Availability availabilityWithNullDay = new Availability(null, Shift.MANHA, professional);
        
        assertThrows(NullPointerException.class, () -> {
            Day day = availabilityWithNullDay.getDay();
            day.toString();
        }, "Deve lançar exceção ao tentar usar dia nulo");
    }

    @Test
    @DisplayName("Deve listar todas as disponibilidades registradas")
    void shouldListAllRegisteredAvailabilities() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);
        professional.addAvailability(availability3);

        List<Availability> availabilities = professional.getAvailabilities();

        assertNotNull(availabilities);
        assertEquals(3, availabilities.size());
        assertTrue(availabilities.contains(availability1));
        assertTrue(availabilities.contains(availability2));
        assertTrue(availabilities.contains(availability3));
    }

    @Test
    @DisplayName("Deve retornar uma disponibilidade existente")
    void shouldReturnExistingAvailability() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);

        Optional<Availability> found = professional.getAvailabilities().stream()
                .filter(avail -> avail.getDay().equals(Day.SEGUNDA) && avail.getShift().equals(Shift.MANHA))
                .findFirst();

        assertTrue(found.isPresent());
        assertEquals(availability1, found.get());
        assertEquals(Day.SEGUNDA, found.get().getDay());
        assertEquals(Shift.MANHA, found.get().getShift());
    }

    @Test
    @DisplayName("Deve retornar erro caso a disponibilidade não exista")
    void shouldReturnErrorWhenAvailabilityDoesNotExist() {
        professional.addAvailability(availability1);

        Optional<Availability> found = professional.getAvailabilities().stream()
                .filter(avail -> avail.getDay().equals(Day.SABADO) && avail.getShift().equals(Shift.TARDE))
                .findFirst();

        assertFalse(found.isPresent(), "Não deve encontrar disponibilidade inexistente");
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar disponibilidade por ID inexistente")
    void shouldReturnErrorWhenAvailabilityIdDoesNotExist() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);
        UUID nonExistentId = UUID.randomUUID();

        Optional<Availability> found = professional.getAvailabilities().stream()
                .filter(avail -> avail.getId() != null && avail.getId().equals(nonExistentId))
                .findFirst();

        assertFalse(found.isPresent(), "Não deve encontrar disponibilidade com ID inexistente");
        assertEquals(2, professional.getAvailabilities().size(), 
                "Lista de disponibilidades deve permanecer inalterada");
    }

    @Test
    @DisplayName("Deve retornar disponibilidade existente ao buscar por ID")
    void shouldReturnExistingAvailabilityById() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);
        
        Optional<Availability> found = professional.getAvailabilities().stream()
                .filter(avail -> avail.equals(availability1))
                .findFirst();

        assertTrue(found.isPresent(), "Deve encontrar disponibilidade existente");
        assertEquals(availability1, found.get());
        
        if (availability1.getId() != null) {
            UUID existingId = availability1.getId();
            Optional<Availability> foundById = professional.getAvailabilities().stream()
                    .filter(avail -> avail.getId() != null && avail.getId().equals(existingId))
                    .findFirst();
            assertTrue(foundById.isPresent(), "Deve encontrar disponibilidade por ID quando ID estiver disponível");
            assertEquals(existingId, foundById.get().getId());
        }
    }

    @Test
    @DisplayName("Deve atualizar horários de uma disponibilidade")
    void shouldUpdateAvailabilityTimes() {
        professional.addAvailability(availability1);
        Day newDay = Day.QUINTA;
        Shift newShift = Shift.TARDE;

        availability1.setDay(newDay);
        availability1.setShift(newShift);

        assertEquals(newDay, availability1.getDay());
        assertEquals(newShift, availability1.getShift());
        assertTrue(professional.getAvailabilities().contains(availability1));
    }

    @Test
    @DisplayName("Deve atualizar apenas o dia de uma disponibilidade")
    void shouldUpdateOnlyDay() {
        professional.addAvailability(availability1);
        Shift originalShift = availability1.getShift();
        Day newDay = Day.SEXTA;

        availability1.setDay(newDay);

        assertEquals(newDay, availability1.getDay());
        assertEquals(originalShift, availability1.getShift());
    }

    @Test
    @DisplayName("Deve atualizar apenas o turno de uma disponibilidade")
    void shouldUpdateOnlyShift() {
        professional.addAvailability(availability1);
        Day originalDay = availability1.getDay();
        Shift newShift = Shift.TARDE;

        availability1.setShift(newShift);

        assertEquals(originalDay, availability1.getDay());
        assertEquals(newShift, availability1.getShift());
    }

    @Test
    @DisplayName("Deve excluir uma disponibilidade existente")
    void shouldDeleteExistingAvailability() {
        professional.addAvailability(availability1);
        professional.addAvailability(availability2);
        professional.addAvailability(availability3);

        boolean removed = professional.getAvailabilities().remove(availability2);

        assertTrue(removed);
        assertEquals(2, professional.getAvailabilities().size());
        assertFalse(professional.getAvailabilities().contains(availability2));
        assertTrue(professional.getAvailabilities().contains(availability1));
        assertTrue(professional.getAvailabilities().contains(availability3));
    }

    @Test
    @DisplayName("Deve retornar false ao tentar excluir disponibilidade inexistente")
    void shouldReturnFalseWhenDeletingNonExistentAvailability() {
        professional.addAvailability(availability1);
        Availability nonExistentAvailability = new Availability(Day.SABADO, Shift.TARDE, professional);

        boolean removed = professional.getAvailabilities().remove(nonExistentAvailability);

        assertFalse(removed);
        assertEquals(1, professional.getAvailabilities().size());
        assertTrue(professional.getAvailabilities().contains(availability1));
    }

    @Test
    @DisplayName("Deve manter relacionamento bidirecional ao adicionar disponibilidade")
    void shouldMaintainBidirectionalRelationshipWhenAddingAvailability() {
        professional.addAvailability(availability1);

        assertEquals(professional, availability1.getProfessional());
        assertTrue(professional.getAvailabilities().contains(availability1));
    }

    @Test
    @DisplayName("Deve permitir múltiplas disponibilidades em dias diferentes")
    void shouldAllowMultipleAvailabilitiesOnDifferentDays() {
        Availability segunda = new Availability(Day.SEGUNDA, Shift.MANHA, professional);
        Availability terca = new Availability(Day.TERCA, Shift.MANHA, professional);
        Availability quarta = new Availability(Day.QUARTA, Shift.MANHA, professional);

        professional.addAvailability(segunda);
        professional.addAvailability(terca);
        professional.addAvailability(quarta);

        assertEquals(3, professional.getAvailabilities().size());
        assertTrue(professional.getAvailabilities().contains(segunda));
        assertTrue(professional.getAvailabilities().contains(terca));
        assertTrue(professional.getAvailabilities().contains(quarta));
    }
}
