package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.patient.domain.model.Parent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParentMapperTest {

    private ParentMapper parentMapper;
    private UUID patientId;

    @BeforeEach
    void setUp() {
        parentMapper = new ParentMapper();
        patientId = UUID.randomUUID();
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenGivenValidDTO() {
        CreateParentDTO dto = new CreateParentDTO("João Pai", "123456789", "123.456.789-00", "Engenheiro", true, "Pai");

        Parent entity = parentMapper.toEntity(dto, patientId);

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.rg(), entity.getRg());
        assertEquals(dto.cpf(), entity.getCpf());
        assertEquals(dto.isAlive(), entity.isAlive());
        assertEquals(dto.profession(), entity.getProfession());
        assertEquals(dto.kinship(), entity.getKinship());
        assertEquals(patientId, entity.getPatientId());
    }

    @Test
    void toEntity_ShouldHandleNullFields_WhenOptionalFieldsAreNull() {
        CreateParentDTO dto = new CreateParentDTO(null, null, null, null, false, null);

        Parent entity = parentMapper.toEntity(dto, patientId);

        assertNotNull(entity);
        assertNull(entity.getName());
        assertNull(entity.getRg());
        assertNull(entity.getCpf());
        assertNull(entity.getProfession());
        assertNull(entity.getKinship());
        assertFalse(entity.isAlive());
        assertEquals(patientId, entity.getPatientId());
    }

    @Test
    void toEntityList_ShouldMapAllItems() {
        List<CreateParentDTO> dtos = List.of(
                new CreateParentDTO("João Pai", "123", "123", "Eng", true, "Pai"),
                new CreateParentDTO("Maria Mãe", "456", "456", "Médica", true, "Mãe")
        );

        List<Parent> entities = parentMapper.toEntityList(dtos, patientId);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("João Pai", entities.get(0).getName());
        assertEquals(patientId, entities.get(0).getPatientId());
        assertEquals("Maria Mãe", entities.get(1).getName());
        assertEquals(patientId, entities.get(1).getPatientId());
    }

    @Test
    void updateEntityListFromDto_ShouldMapAllItems() {
        List<UpdateParentDTO> dtos = List.of(
                new UpdateParentDTO("João Pai Editado", "123", "123", "Aposentado", Boolean.FALSE, "Pai")
        );

        List<Parent> entities = parentMapper.updateEntityListFromDto(dtos, patientId);

        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertEquals(dtos.get(0).name(), entities.get(0).getName());
        assertFalse(entities.get(0).isAlive());
        assertEquals(patientId, entities.get(0).getPatientId());
    }

    @Test
    void toEntityListFromResponse_ShouldMapAllItems() {
        UUID id = UUID.randomUUID();
        List<ParentResponseDTO> dtos = List.of(
                new ParentResponseDTO(id, "João Pai", "123", "123", "Eng", "Pai", true)
        );

        List<Parent> entities = parentMapper.toEntityListFromResponse(dtos, patientId);

        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertEquals(id, entities.get(0).getId());
        assertEquals("João Pai", entities.get(0).getName());
        assertEquals(patientId, entities.get(0).getPatientId());
    }

    @Test
    void toResponseDTO_ShouldMapAllFields() {
        Parent entity = new Parent(UUID.randomUUID(), "João Pai", "123", "123", true, "Engenheiro", "Pai", patientId);

        ParentResponseDTO dto = parentMapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.id());
        assertEquals(entity.getName(), dto.name());
        assertEquals(entity.getRg(), dto.rg());
        assertEquals(entity.getCpf(), dto.cpf());
        assertEquals(entity.isAlive(), dto.isAlive());
        assertEquals(entity.getProfession(), dto.profession());
        assertEquals(entity.getKinship(), dto.kinship());
    }

    @Test
    void toResponseDTOList_ShouldMapAllItems() {
        List<Parent> entities = List.of(
                new Parent(UUID.randomUUID(), "João Pai", "123", "123", true, "Engenheiro", "Pai", patientId)
        );

        List<ParentResponseDTO> dtos = parentMapper.toResponseDTOList(entities);

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals(entities.get(0).getId(), dtos.get(0).id());
    }
}
