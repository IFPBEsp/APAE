package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.domain.model.Vaccine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VaccineMapperTest {

    private VaccineMapper vaccineMapper;

    @BeforeEach
    void setUp() {
        vaccineMapper = new VaccineMapper();
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenGivenValidDTO() {
        CreateVaccineDTO dto = new CreateVaccineDTO("Covid-19");

        Vaccine entity = vaccineMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
    }

    @Test
    void toEntity_ShouldThrow_WhenNameIsNull() {
        CreateVaccineDTO dto = new CreateVaccineDTO(null);
        assertThrows(IllegalArgumentException.class, () -> vaccineMapper.toEntity(dto));
    }

    @Test
    void toEntitySet_ShouldMapAllItems() {
        CreateVaccineDTO dto1 = new CreateVaccineDTO("Covid-19");
        CreateVaccineDTO dto2 = new CreateVaccineDTO("Influenza");

        Vaccine v1 = vaccineMapper.toEntity(dto1);
        Vaccine v2 = vaccineMapper.toEntity(dto2);

        assertEquals(dto1.name(), v1.getName());
        assertEquals(dto2.name(), v2.getName());
    }

    @Test
    void toEntitySetFromResponse_ShouldMapAllFields() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Set<VaccineResponseDTO> dtos = Set.of(
                new VaccineResponseDTO(id1, "Covid-19", false),
                new VaccineResponseDTO(id2, "Influenza", false)
        );

        Set<Vaccine> entities = vaccineMapper.toEntitySetFromResponse(dtos);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> id1.equals(e.getId()) && "Covid-19".equals(e.getName())));
        assertTrue(entities.stream().anyMatch(e -> id2.equals(e.getId()) && "Influenza".equals(e.getName())));
    }

    @Test
    void toResponseDTO_ShouldMapAllFields() {
        Vaccine entity = new Vaccine(UUID.randomUUID(), "Covid-19");

        VaccineResponseDTO dto = vaccineMapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertFalse(dto.hasPatient());
    }

    @Test
    void toResponseDTOSet_ShouldMapAllItems() {
        Vaccine v1 = new Vaccine(UUID.randomUUID(), "Covid-19");
        Vaccine v2 = new Vaccine(UUID.randomUUID(), "Influenza");
        Set<Vaccine> entities = Set.of(v1, v2);

        Set<VaccineResponseDTO> dtos = vaccineMapper.toResponseDTOSet(entities);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertTrue(dtos.stream().anyMatch(d -> v1.getId().equals(d.id()) && v1.getName().equals(d.name())));
        assertTrue(dtos.stream().anyMatch(d -> v2.getId().equals(d.id()) && v2.getName().equals(d.name())));
    }
}
