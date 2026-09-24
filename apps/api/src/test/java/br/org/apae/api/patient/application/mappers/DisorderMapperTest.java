package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.Disorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DisorderMapperTest {

    private DisorderMapper disorderMapper;

    @BeforeEach
    void setUp() {
        disorderMapper = new DisorderMapper();
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenGivenValidDTO() {
        CreateDisorderDTO dto = new CreateDisorderDTO("Autismo");

        Disorder entity = disorderMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
    }

    @Test
    void toEntity_ShouldHandleNullFields_WhenOptionalFieldsAreNull() {
        CreateDisorderDTO dto = new CreateDisorderDTO(null);

        Disorder entity = disorderMapper.toEntity(dto);

        assertNotNull(entity);
        assertNull(entity.getName());
    }

    @Test
    void toEntitySet_ShouldMapAllItems() {
        Set<CreateDisorderDTO> dtos = Set.of(
                new CreateDisorderDTO("Autismo"),
                new CreateDisorderDTO("TDAH")
        );

        Set<Disorder> entities = disorderMapper.toEntitySet(dtos);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> "Autismo".equals(e.getName())));
        assertTrue(entities.stream().anyMatch(e -> "TDAH".equals(e.getName())));
    }

    @Test
    void toEntityFromResponse_ShouldMapAllFields() {
        UUID id = UUID.randomUUID();
        DisorderResponseDTO dto = new DisorderResponseDTO(id, "Autismo", false);

        Disorder entity = disorderMapper.toEntityFromResponse(dto);

        assertNotNull(entity);
        assertEquals(dto.id(), entity.getId());
        assertEquals(dto.name(), entity.getName());
    }

    @Test
    void toEntitySetFromResponse_ShouldMapAllItems() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Set<DisorderResponseDTO> dtos = Set.of(
                new DisorderResponseDTO(id1, "Autismo", false),
                new DisorderResponseDTO(id2, "TDAH", false)
        );

        Set<Disorder> entities = disorderMapper.toEntitySetFromResponse(dtos);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> id1.equals(e.getId()) && "Autismo".equals(e.getName())));
        assertTrue(entities.stream().anyMatch(e -> id2.equals(e.getId()) && "TDAH".equals(e.getName())));
    }

    @Test
    void toResponseDTO_ShouldMapAllFields() {
        Disorder entity = new Disorder(UUID.randomUUID(), "Autismo");

        DisorderResponseDTO dto = disorderMapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertFalse(dto.hasPatient());
    }

    @Test
    void toResponseDTOSet_ShouldMapAllItems() {
        Disorder d1 = new Disorder(UUID.randomUUID(), "Autismo");
        Disorder d2 = new Disorder(UUID.randomUUID(), "TDAH");
        Set<Disorder> entities = Set.of(d1, d2);

        Set<DisorderResponseDTO> dtos = disorderMapper.toResponseDTOSet(entities);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertTrue(dtos.stream().anyMatch(d -> d1.getId().equals(d.id()) && d1.getName().equals(d.name())));
        assertTrue(dtos.stream().anyMatch(d -> d2.getId().equals(d.id()) && d2.getName().equals(d.name())));
    }
}
