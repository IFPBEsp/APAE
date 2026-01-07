package br.org.apae.api.unit.mocks;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;

public final class HealthProfessionalMockDto {

    private HealthProfessionalMockDto() {}

    public static final UUID PROFESSIONAL_ID_1 =
        UUID.fromString("11111111-1111-1111-1111-111111111111");

    public static final UUID PROFESSIONAL_ID_2 =
        UUID.fromString("22222222-2222-2222-2222-222222222222");

    public static final UUID ADDRESS_ID_1 =
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    public static final UUID ADDRESS_ID_2 =
        UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    public static CreateAddressDTO createAddressRequest() {
        return new CreateAddressDTO(
            "São Paulo",
            "01000-000",
            "SP",
            "Centro",
            "Rua A",
            "123",
            "Apto 10"
        );
    }

    public static CreateHealthProfessionalDTO createHealthProfessionalRequest() {
        return new CreateHealthProfessionalDTO(
            "Fisioterapia",
            "11999999999",
            "CREFITO-12345",
            "teste@apae.org.br",
            "João da Silva",
            "123456789",
            createAddressRequest()
        );
    }

    public static AddressResponseDTO createAddressResponse1() {
        return new AddressResponseDTO(
            ADDRESS_ID_1,
            "01000-000",
            "São Paulo",
            "SP",
            "Centro",
            "Rua A",
            "123",
            "Apto 10"
        );
    }

    public static AddressResponseDTO createAddressResponse2() {
        return new AddressResponseDTO(
            ADDRESS_ID_2,
            "02000-000",
            "São Paulo",
            "SP",
            "Santana",
            "Rua B",
            "456",
            null
        );
    }

    public static HealthProfessionalResponseDTO createProfessionalResponse1() {
        return new HealthProfessionalResponseDTO(
            PROFESSIONAL_ID_1,
            "Fisioterapia",
            "11999999999",
            "CREFITO-12345",
            "teste@apae.org.br",
            "João da Silva",
            "123456789",
            createAddressResponse1()
        );
    }

    public static HealthProfessionalResponseDTO createProfessionalResponse2() {
        return new HealthProfessionalResponseDTO(
            PROFESSIONAL_ID_2,
            "Psicologia",
            "11888888888",
            "CRP-54321",
            "maria@apae.org.br",
            "Maria Souza",
            "987654321",
            createAddressResponse2()
        );
    }

    public static List<HealthProfessionalResponseDTO> createProfessionalResponseList() {
        return List.of(
            createProfessionalResponse1(),
            createProfessionalResponse2()
        );
    }
}
