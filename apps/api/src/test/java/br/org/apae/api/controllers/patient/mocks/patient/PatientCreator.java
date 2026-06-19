package br.org.apae.api.controllers.patient.mocks.patient;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PatientCreator {

    private static CreateAddressDTO createAddress() {
        return new CreateAddressDTO(
                "Campina Grande",
                "58400-000",
                "PB",
                "Centro",
                "Rua Quinze de Novembro",
                "100",
                "Apto 101"
        );
    }

    private static CreateVaccineDTO createVaccine() {
        return new CreateVaccineDTO("BCG");
    }

    private static CreateDisorderDTO createDisorder() {
        return new CreateDisorderDTO("Transtorno do Espectro Autista");
    }

    private static CreateServiceTypeDTO createServiceType() {
        return new CreateServiceTypeDTO("Psicologia");
    }

    private static CreateGuardianDTO createGuardian() {
        return new CreateGuardianDTO(
                "Maria da Silva",
                "83988887777",
                "MÃE",
                createAddress()
        );
    }

    private static List<CreateParentDTO> createParents() {
        return List.of(
                new CreateParentDTO(
                        "José da Silva",
                        "33344455",
                        "000.111.222-33",
                        "Autônomo",
                        true,
                        "PAI"
                )
        );
    }
/*
    private static CreateAnnualRegistryDTO createAnnualRegistry() {
        return new CreateAnnualRegistryDTO(
                "123456789",
                "Nenhuma doença pré-existente",
                "Nenhum",
                new BigDecimal("1412.00"),
                2024,
                Set.of(createDisorder()),
                Set.of(createServiceType())
        );
    }
*/
    private static UpdateGuardianDTO createUpdateGuardian() {
        return new UpdateGuardianDTO(
                "Maria da Silva Editada",
                "83988887777",
                "MÃE",
                createAddress()
        );
    }

    private static List<UpdateParentDTO> createUpdateParents() {
        return List.of(
                new UpdateParentDTO(
                        "José da Silva Editado",
                        "33344455",
                        "000.111.222-33",
                        "Autônomo",
                        true,
                        "PAI"
                )
        );
    }

    public static PatientResponseDTO createResponse() {
        UUID id = UUID.randomUUID();

        AddressResponseDTO address = new AddressResponseDTO(
                UUID.randomUUID(), "58000-000", "Campina Grande", "PB",
                "Centro", "Rua X", "123", "Apt 1");

        GuardianResponseDTO guardian = new GuardianResponseDTO(
                UUID.randomUUID(), "Mãe", "8399999999", "MÃE", address);

        ParentResponseDTO parent = new ParentResponseDTO(
                UUID.randomUUID(), "Pai", "123456", "000.000.000-00",
                "Autônomo", "PAI", true);

        VaccineResponseDTO vaccine = new VaccineResponseDTO(UUID.randomUUID(), "BCG", false);

        return new PatientResponseDTO(
                id,
                "João da Silva",
                "Campina Grande",
                LocalDate.of(2010, 1, 1),
                "8399999999",
                "123456",
                "Cartório X",
                "10",
                "A",
                "123456",
                LocalDate.of(2015, 1, 1),
                "SSP/PB",
                "000.000.000-00",
                "123456789",
                "12345",
                LocalDate.now(),
                "Nenhuma",
                true,
                false,
                address,
                guardian,
                List.of(parent),
                Set.of(vaccine),
                "http://url-foto.com"
        );
    }

    public static PatientSummaryResponseDTO createSummaryResponse() {
        UUID id = UUID.randomUUID();

        AddressResponseDTO address = new AddressResponseDTO(
                UUID.randomUUID(), "58000-000", "Campina Grande", "PB",
                "Centro", "Rua X", "123", "Apt 1");

        return new PatientSummaryResponseDTO(
                id,
                "João da Silva",
                "Campina Grande",
                LocalDate.of(2010, 1, 1),
                "8399999999",
                "123456",
                "Cartório X",
                "10",
                "A",
                "123456",
                LocalDate.of(2015, 1, 1),
                "SSP/PB",
                "000.000.000-00",
                "123456789",
                "12345",
                LocalDate.now(),
                "Nenhuma",
                true,
                false,
                address,
                "http://url-foto.com");
    }
/*
    public static CreatePatientDTO createRequest() {
        return new CreatePatientDTO(
                "João da Silva",
                "Brasileira",
                LocalDate.of(2010, 1, 1),
                "8399999",
                "123",
                "Cartório",
                "1",
                "A",
                "RG123",
                LocalDate.now(),
                "SSP",
                "00000000000",
                "123",
                "123",
                LocalDate.now(),
                "Nenhuma",
                true,
                createAddress(),
                createGuardian(),
                createParents(),
                Set.of(createVaccine()),
                createAnnualRegistry()
        );
    }
*/
    public static UpdatePatientDTO createUpdatePayload() {
    return new UpdatePatientDTO(
            "João da Silva Editado",
            "Brasileira",
            LocalDate.of(2010, 1, 1),
            "8399999-EDIT",
            "123",
            "Cartório Novo",
            "1",
            "A",
            "RG123",
            LocalDate.of(2015, 1, 1),
            "SSP/PB",
            "000.000.000-00",
            "123456789",
            "12345",
            "Nenhuma",
            true,
            createAddress(),
            createUpdateGuardian(),
            createUpdateParents(),
            Set.of(createVaccine())
    );
}
/*
    public static CreatePatientDTO createInvalidRequest() {
        return new CreatePatientDTO(
                "",
                "Brasileira",
                LocalDate.now().plusDays(1),
                "8399999",
                "123",
                "Cartório",
                "1",
                "A",
                "",
                LocalDate.now(),
                "SSP",
                "00000000000",
                "123",
                "123",
                LocalDate.now(),
                "Nenhuma",
                true,
                createAddress(),
                createGuardian(),
                createParents(),
                Set.of(createVaccine()),
                createAnnualRegistry()
        );
    }
*/
    public static UpdatePatientDTO createInvalidUpdateRequest() {
        return new UpdatePatientDTO(
                "",
                "Brasileira",
                LocalDate.now().plusDays(1),
                "8399999",
                "123",
                "Cartório",
                "1",
                "A",
                "RG123",
                LocalDate.now(),
                "SSP",
                "000",
                "123",
                "123",
                "Nenhuma",
                true,
                PatientCreator.createAddress(),
                PatientCreator.createUpdateGuardian(),
                PatientCreator.createUpdateParents(),
                Set.of(createVaccine())
        );
    }

    public static CreateDocumentsDTO createDocuments() {
        MultipartFile rg = new MockMultipartFile("rg", "rg.pdf", "application/pdf", "conteudo falso".getBytes());
        MultipartFile cpf = new MockMultipartFile("cpf", "cpf.pdf", "application/pdf", "conteudo falso".getBytes());
        MultipartFile address = new MockMultipartFile("proof_of_address", "address.pdf", "application/pdf", "conteudo falso".getBytes());
        MultipartFile birth = new MockMultipartFile("birth_certificate", "birth.pdf", "application/pdf", "conteudo falso".getBytes());
        MultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", "imagem falsa".getBytes());

        List<MultipartFile> reports = List.of(
                new MockMultipartFile("reports", "laudo1.pdf", "application/pdf", "conteudo falso".getBytes())
        );

        List<MultipartFile> referrals = List.of(
                new MockMultipartFile("referrals", "encaminhamento.pdf", "application/pdf", "conteudo falso".getBytes())
        );

        return new CreateDocumentsDTO(
                rg,
                cpf,
                address,
                birth,
                photo,
                reports,
                referrals
        );
    }
}
