package br.org.apae.api.address.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;

class AddressMapperTest {

    private static final UUID ADDRESS_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final String CITY = "Sao Paulo";
    private static final String CEP = "01000-000";
    private static final String STATE = "SP";
    private static final String NEIGHBORHOOD = "Centro";
    private static final String STREET = "Rua A";
    private static final String NUMBER = "123";
    private static final String COMPLEMENT = "Apto 10";

    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        addressMapper = new AddressMapper();
    }

    @Test
    @DisplayName("toEntity deve mapear todos os campos e deixar o id nulo")
    void toEntity_ShouldMapAllFields() {
        CreateAddressDTO dto = new CreateAddressDTO(CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, COMPLEMENT);

        Address entity = addressMapper.toEntity(dto);

        assertNull(entity.getId());
        assertEquals(CITY, entity.getCity());
        assertEquals(CEP, entity.getCep());
        assertEquals(STATE, entity.getState());
        assertEquals(NEIGHBORHOOD, entity.getNeighborhood());
        assertEquals(STREET, entity.getStreet());
        assertEquals(NUMBER, entity.getNumber());
        assertEquals(COMPLEMENT, entity.getComplement());
    }

    @Test
    @DisplayName("toEntity deve normalizar complemento em branco para nulo")
    void toEntity_ShouldNormalizeBlankComplementToNull() {
        CreateAddressDTO dto = new CreateAddressDTO(CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, "   ");

        Address entity = addressMapper.toEntity(dto);

        assertNull(entity.getComplement());
        assertEquals(STREET, entity.getStreet());
    }

    @Test
    @DisplayName("toEntity deve manter complemento nulo como nulo")
    void toEntity_ShouldKeepNullComplement() {
        CreateAddressDTO dto = new CreateAddressDTO(CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, null);

        Address entity = addressMapper.toEntity(dto);

        assertNull(entity.getComplement());
    }

    @Test
    @DisplayName("toEntityFromResponse deve mapear o id e todos os campos")
    void toEntityFromResponse_ShouldMapIdAndAllFields() {
        AddressResponseDTO dto = new AddressResponseDTO(ADDRESS_ID, CEP, CITY, STATE, NEIGHBORHOOD, STREET, NUMBER,
                COMPLEMENT);

        Address entity = addressMapper.toEntityFromResponse(dto);

        assertEquals(ADDRESS_ID, entity.getId());
        assertEquals(CITY, entity.getCity());
        assertEquals(CEP, entity.getCep());
        assertEquals(STATE, entity.getState());
        assertEquals(NEIGHBORHOOD, entity.getNeighborhood());
        assertEquals(STREET, entity.getStreet());
        assertEquals(NUMBER, entity.getNumber());
        assertEquals(COMPLEMENT, entity.getComplement());
    }

    @Test
    @DisplayName("toEntityFromResponse deve normalizar complemento em branco para nulo")
    void toEntityFromResponse_ShouldNormalizeBlankComplementToNull() {
        AddressResponseDTO dto = new AddressResponseDTO(ADDRESS_ID, CEP, CITY, STATE, NEIGHBORHOOD, STREET, NUMBER, "");

        Address entity = addressMapper.toEntityFromResponse(dto);

        assertNull(entity.getComplement());
        assertEquals(ADDRESS_ID, entity.getId());
    }

    @Test
    @DisplayName("toEntityFromResponse deve manter complemento nulo como nulo")
    void toEntityFromResponse_ShouldKeepNullComplement() {
        AddressResponseDTO dto = new AddressResponseDTO(ADDRESS_ID, CEP, CITY, STATE, NEIGHBORHOOD, STREET, NUMBER, null);

        Address entity = addressMapper.toEntityFromResponse(dto);

        assertNull(entity.getComplement());
        assertEquals(ADDRESS_ID, entity.getId());
    }

    @Test
    @DisplayName("updateEntityFromDto deve criar um novo endereco quando o endereco atual e nulo")
    void updateEntityFromDto_ShouldCreateNewAddress_WhenAddressIsNull() {
        UpdateAddressDTO dto = new UpdateAddressDTO("Curitiba", "80000-000", "PR", "Batel", "Rua B", "456", "Sala 2");

        Address result = addressMapper.updateEntityFromDto(null, dto);

        assertNull(result.getId());
        assertEquals("Curitiba", result.getCity());
        assertEquals("80000-000", result.getCep());
        assertEquals("PR", result.getState());
        assertEquals("Batel", result.getNeighborhood());
        assertEquals("Rua B", result.getStreet());
        assertEquals("456", result.getNumber());
        assertEquals("Sala 2", result.getComplement());
    }

    @Test
    @DisplayName("updateEntityFromDto deve alterar a mesma instancia com todos os campos do DTO")
    void updateEntityFromDto_ShouldUpdateExistingAddress() {
        Address address = new Address(ADDRESS_ID, CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, COMPLEMENT);
        UpdateAddressDTO dto = new UpdateAddressDTO("Curitiba", "80000-000", "PR", "Batel", "Rua B", "456", "Sala 2");

        Address result = addressMapper.updateEntityFromDto(address, dto);

        assertSame(address, result);
        assertEquals(ADDRESS_ID, result.getId());
        assertEquals("Curitiba", result.getCity());
        assertEquals("80000-000", result.getCep());
        assertEquals("PR", result.getState());
        assertEquals("Batel", result.getNeighborhood());
        assertEquals("Rua B", result.getStreet());
        assertEquals("456", result.getNumber());
        assertEquals("Sala 2", result.getComplement());
    }

    @Test
    @DisplayName("updateEntityFromDto nao normaliza complemento em branco, ao contrario de toEntity")
    void updateEntityFromDto_ShouldKeepComplementAsProvided_WhenBlank() {
        Address address = new Address(ADDRESS_ID, CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, COMPLEMENT);
        UpdateAddressDTO dto = new UpdateAddressDTO(CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, "   ");

        Address result = addressMapper.updateEntityFromDto(address, dto);

        assertEquals("   ", result.getComplement());
    }

    @Test
    @DisplayName("updateEntityFromDto nao reaproveita a instancia anterior quando o endereco recebido e nulo")
    void updateEntityFromDto_ShouldNotReusePreviousInstance_WhenAddressIsNull() {
        UpdateAddressDTO dto = new UpdateAddressDTO(CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, COMPLEMENT);

        Address first = addressMapper.updateEntityFromDto(null, dto);
        Address second = addressMapper.updateEntityFromDto(null, dto);

        assertNotSame(first, second);
    }

    @Test
    @DisplayName("toResponseDTO deve mapear todos os campos da entidade")
    void toResponseDTO_ShouldMapAllFields() {
        Address address = new Address(ADDRESS_ID, CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, COMPLEMENT);

        AddressResponseDTO dto = addressMapper.toResponseDTO(address);

        assertEquals(ADDRESS_ID, dto.id());
        assertEquals(CEP, dto.cep());
        assertEquals(CITY, dto.city());
        assertEquals(STATE, dto.state());
        assertEquals(NEIGHBORHOOD, dto.neighborhood());
        assertEquals(STREET, dto.street());
        assertEquals(NUMBER, dto.number());
        assertEquals(COMPLEMENT, dto.complement());
    }

    @Test
    @DisplayName("toResponseDTO deve propagar complemento nulo")
    void toResponseDTO_ShouldMapNullComplement_WhenComplementIsNull() {
        Address address = new Address(ADDRESS_ID, CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, null);

        AddressResponseDTO dto = addressMapper.toResponseDTO(address);

        assertNull(dto.complement());
        assertEquals(ADDRESS_ID, dto.id());
        assertEquals(NUMBER, dto.number());
    }
}
