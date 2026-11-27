package br.org.apae.api.patient.application.internal;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.application.mappers.VaccineMapper;
import br.org.apae.api.patient.domain.exceptions.VaccineMismatchException;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.VaccineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("patient")
@Tag("unit")
class VaccineApplicationServiceImplTest {

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private VaccineMapper vaccineMapper;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private VaccineApplicationServiceImpl vaccineApplicationService;

    @Test
    @DisplayName("Deve retornar conjunto vazio sem consultar o banco quando lista de vacinas for vazia")
    void shouldReturnEmptySetWhenVaccinesListIsEmpty() {
        Set<VaccineResponseDTO> result = vaccineApplicationService.findVaccines(Set.of());

        assertTrue(result.isEmpty());
        verifyNoInteractions(vaccineRepository, vaccineMapper);
    }

    @Test
    @DisplayName("Deve retornar conjunto vazio sem consultar o banco quando lista de vacinas for nula")
    void shouldReturnEmptySetWhenVaccinesListIsNull() {
        Set<VaccineResponseDTO> result = vaccineApplicationService.findVaccines(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(vaccineRepository, vaccineMapper);
    }

    @Test
    @DisplayName("Deve retornar vacinas encontradas quando todas existirem")
    void shouldReturnMatchingVaccinesWhenAllExist() {
        Set<CreateVaccineDTO> input = Set.of(new CreateVaccineDTO("BCG"));
        UUID id = UUID.randomUUID();
        Set<Vaccine> entities = Set.of(new Vaccine(id, "BCG"));
        Set<VaccineResponseDTO> expected = Set.of(new VaccineResponseDTO(id, "BCG", false));

        when(vaccineRepository.findByNameInIgnoreCase(Set.of("BCG"))).thenReturn(entities);
        when(vaccineMapper.toResponseDTOSet(entities)).thenReturn(expected);

        Set<VaccineResponseDTO> result = vaccineApplicationService.findVaccines(input);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Deve lançar VaccineMismatchException quando alguma vacina não for encontrada")
    void shouldThrowMismatchExceptionWhenVaccineNotFound() {
        Set<CreateVaccineDTO> input = Set.of(
                new CreateVaccineDTO("BCG"),
                new CreateVaccineDTO("Inexistente")
        );
        Set<Vaccine> entities = Set.of(new Vaccine(UUID.randomUUID(), "BCG"));

        when(vaccineRepository.findByNameInIgnoreCase(Set.of("BCG", "Inexistente"))).thenReturn(entities);

        assertThrows(VaccineMismatchException.class, () -> vaccineApplicationService.findVaccines(input));
    }
}
