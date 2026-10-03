package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.patient.domain.model.Guardian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardianMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private GuardianMapper guardianMapper;

    private UUID patientId;
    private Address dummyAddress;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        dummyAddress = new Address("Cidade", "12345-678", "SP", "Bairro", "Rua A", "123", "Complemento");
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenGivenValidDTO() {
        CreateAddressDTO addressDto = new CreateAddressDTO("Cidade", "12345-678", "SP", "Bairro", "Rua A", "123", "Complemento");
        CreateGuardianDTO dto = new CreateGuardianDTO("Guardian Name", "99999-9999", "Mãe", addressDto);

        when(addressMapper.toEntity(addressDto)).thenReturn(dummyAddress);

        Guardian entity = guardianMapper.toEntity(dto, patientId);

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.contact(), entity.getContact());
        assertEquals(dto.kinship(), entity.getKinship());
        assertEquals(dummyAddress, entity.getAddress());
        assertEquals(patientId, entity.getPatientId());
        verify(addressMapper, times(1)).toEntity(addressDto);
    }

    @Test
    void toEntity_ShouldHandleNullFields_WhenOptionalFieldsAreNull() {
        CreateGuardianDTO dto = new CreateGuardianDTO(null, null, null, null);
        when(addressMapper.toEntity(null)).thenReturn(null);

        Guardian entity = guardianMapper.toEntity(dto, patientId);

        assertNotNull(entity);
        assertNull(entity.getName());
        assertNull(entity.getContact());
        assertNull(entity.getKinship());
        assertNull(entity.getAddress());
        assertEquals(patientId, entity.getPatientId());
    }

    @Test
    void toEntityFromResponse_ShouldMapAllFields() {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(UUID.randomUUID(), "Cidade", "12345-678", "SP", "Bairro", "Rua A", "123", "Complemento");
        GuardianResponseDTO dto = new GuardianResponseDTO(UUID.randomUUID(), "Guardian Name", "99999-9999", "Mãe", addressResponseDTO);

        when(addressMapper.toEntityFromResponse(addressResponseDTO)).thenReturn(dummyAddress);

        Guardian entity = guardianMapper.toEntityFromResponse(dto, patientId);

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
        assertEquals(dto.contact(), entity.getContact());
        assertEquals(dto.kinship(), entity.getKinship());
        assertEquals(dummyAddress, entity.getAddress());
        assertEquals(patientId, entity.getPatientId());
    }

    @Test
    void updateEntityFromDto_ShouldMapAllFields() {
        Guardian guardian = new Guardian(UUID.randomUUID(), "Old Name", "00000", "Avó", dummyAddress, patientId);
        CreateAddressDTO addressDto = new CreateAddressDTO("Cidade 2", "87654-321", "RJ", "Bairro 2", "Rua B", "321", "Ap 2");
        UpdateGuardianDTO dto = new UpdateGuardianDTO("New Name", "11111", "Pai", addressDto);

        Address newAddress = new Address("Cidade 2", "87654-321", "RJ", "Bairro 2", "Rua B", "321", "Ap 2");
        when(addressMapper.toEntity(addressDto)).thenReturn(newAddress);

        Guardian updatedEntity = guardianMapper.updateEntityFromDto(guardian, dto, patientId);

        assertNotNull(updatedEntity);
        assertEquals(guardian.getId(), updatedEntity.getId());
        assertEquals(dto.name(), updatedEntity.getName());
        assertEquals(dto.contact(), updatedEntity.getContact());
        assertEquals(dto.kinship(), updatedEntity.getKinship());
        assertEquals(newAddress, updatedEntity.getAddress());
        assertEquals(patientId, updatedEntity.getPatientId());
    }

    @Test
    void toResponseDTO_ShouldMapAllFields() {
        Guardian guardian = new Guardian(UUID.randomUUID(), "Guardian Name", "99999-9999", "Mãe", dummyAddress, patientId);

        GuardianResponseDTO dto = guardianMapper.toResponseDTO(guardian);

        assertNotNull(dto);
        assertEquals(guardian.getId(), dto.id());
        assertEquals(guardian.getName(), dto.name());
        assertEquals(guardian.getContact(), dto.contact());
        assertEquals(guardian.getKinship(), dto.kinship());
        assertNotNull(dto.address());
    }
}
