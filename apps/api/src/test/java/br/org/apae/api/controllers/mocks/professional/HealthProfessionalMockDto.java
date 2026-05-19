package br.org.apae.api.controllers.mocks.professional;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;

public final class HealthProfessionalMockDto {

    private HealthProfessionalMockDto() {}

    public static final UUID PROFESSIONAL_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final UUID PROFESSIONAL_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID ADDRESS_ID_1 = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    public static final UUID ADDRESS_ID_2 = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    
    public static MockMultipartFile volunteerAgreementFile() {
        return new MockMultipartFile("volunteerAgreement", "volunteer-agreement.pdf", MediaType.APPLICATION_PDF_VALUE, "fake volunteer agreement content".getBytes());
    }

    public static MockMultipartFile curriculumFile() {
        return new MockMultipartFile("curriculum", "curriculum.pdf", MediaType.APPLICATION_PDF_VALUE, "fake curriculum content".getBytes());
    }

    public static MockMultipartFile attachmentAnyFile() {
        return new MockMultipartFile("attachmentAny", "attachment.txt", MediaType.TEXT_PLAIN_VALUE, "optional attachment content".getBytes());
    }

    public static CreateProfessionalDocumentsDTO createProfessionalDocumentsRequest() {
        return new CreateProfessionalDocumentsDTO(volunteerAgreementFile(), curriculumFile(), attachmentAnyFile());
    }

    public static CreateProfessionalDocumentsDTO createProfessionalDocumentsRequestWithoutOptional() {
        return new CreateProfessionalDocumentsDTO(volunteerAgreementFile(), curriculumFile(), null);
    }

    public static CreateServiceAreaDTO createServiceAreaRequestPhysiotherapy() {
        return new CreateServiceAreaDTO("Fisioterapia");
    }

    public static CreateServiceAreaDTO createServiceAreaRequestPsychology() {
        return new CreateServiceAreaDTO("Psicologia");
    }

    public static ServiceAreaResponseDTO createServiceAreaResponsePhysiotherapy() {
        return new ServiceAreaResponseDTO(1, "Fisioterapia");
    }

    public static ServiceAreaResponseDTO createServiceAreaResponsePsychology() {
        return new ServiceAreaResponseDTO(2, "Psicologia");
    }

    public static CreateAddressDTO createAddressRequest() {
        return new CreateAddressDTO("São Paulo", "01000-000", "SP", "Centro", "Rua A", "123", "Apto 10");
    }

    public static CreateHealthProfessionalDTO createHealthProfessionalRequest() {
        return new CreateHealthProfessionalDTO(
            createServiceAreaRequestPsychology(),
            "11999999999",
            "CRP-12345",
            "teste@apae.org.br",
            "João da Silva",
            "123456789",
            createAddressRequest(),
            List.of(
                new CreateAvailabilityDTO("SEGUNDA", "MANHA"),
                new CreateAvailabilityDTO("TERCA", "TARDE")
            ),
            "http://example.com/photo.jpg" // Adicionado o parâmetro faltante do CreateHealthProfessionalDTO
        );
    }

    public static AddressResponseDTO createAddressResponse1() {
        return new AddressResponseDTO(ADDRESS_ID_1, "01000-000", "São Paulo", "SP", "Centro", "Rua A", "123", "Apto 10");
    }

    public static AddressResponseDTO createAddressResponse2() {
        return new AddressResponseDTO(ADDRESS_ID_2, "02000-000", "São Paulo", "SP", "Santana", "Rua B", "456", null);
    }

    public static HealthProfessionalResponseDTO createProfessionalResponse1() {
        return new HealthProfessionalResponseDTO(
            PROFESSIONAL_ID_1,
            "João da Silva",
            "teste@apae.org.br",
            "CREFITO-12345",
            "123456789",
            "11999999999",
            "Fisioterapia",
            true,
            createAddressResponse1(),
            createServiceAreaResponsePhysiotherapy(),
            List.of(
                new AvailabilityResponseDTO(UUID.randomUUID(), "SEGUNDA", "MANHA"),
                new AvailabilityResponseDTO(UUID.randomUUID(), "TERCA", "TARDE")
            ),
            "http://example.com/photo1.jpg" // Adicionado o parâmetro faltante do HealthProfessionalResponseDTO
        );
    }

    public static HealthProfessionalResponseDTO createProfessionalResponse2() {
        return new HealthProfessionalResponseDTO(
            PROFESSIONAL_ID_2,
            "Maria Souza",
            "maria@apae.org.br",
            "CRP-54321",
            "987654321",
            "11888888888",
            "Psicologia",
            true,
            createAddressResponse2(),
            createServiceAreaResponsePsychology(),
            List.of(),
            null // Adicionado o parâmetro faltante (null) do HealthProfessionalResponseDTO
        );
    }

    public static List<HealthProfessionalResponseDTO> createProfessionalResponseList() {
        return List.of(
            createProfessionalResponse1(),
            createProfessionalResponse2()
        );
    }
}