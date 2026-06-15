package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.controllers.patient.mocks.patient.PatientCreator;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.domain.exceptions.PatientConflictException;
import br.org.apae.api.servicetype.application.interfaces.ServiceTypeApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("patient")
})
@WebMvcTest(PatientControllerImpl.class)
public class PatientRegistrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientApplicationService patientService;

    @MockitoBean
    private DisorderApplicationService disorderApplicationService;

    @MockitoBean
    private AnnualRegistryApplicationService annualRegistryApplicationService;

    @MockitoBean
    private ServiceTypeApplicationService serviceAreaApplicationService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private CreateAnnualRegistryDTO createAnnualRegistry() {
        return new CreateAnnualRegistryDTO(
                "123456789",
                "Nenhuma doença pré-existente",
                "Nenhum",
                new BigDecimal("1412.00"),
                Year.of(2024),
                Set.of(new CreateDisorderDTO("TEA")),
                Set.of(new CreateServiceTypeDTO("Psicologia"))
        );
    }

    private CreatePatientDTO createRequest() {
        return new CreatePatientDTO(
                "João da Silva",
                "Brasileira",
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
                new CreateAddressDTO(
                        "Campina Grande",
                        "58000-000",
                        "PB",
                        "Centro",
                        "Rua X",
                        "123",
                        "Apt 1"
                ),
                new CreateGuardianDTO(
                        "Mãe",
                        "8399999999",
                        "MÃE",
                        new CreateAddressDTO(
                                "Campina Grande",
                                "58000-000",
                                "PB",
                                "Centro",
                                "Rua X",
                                "123",
                                "Apt 1"
                        )
                ),
                List.of(
                        new CreateParentDTO(
                                "Pai",
                                "123456",
                                "000.000.000-00",
                                "Autônomo",
                                true,
                                "PAI"
                        )
                ),
                Set.of(new CreateVaccineDTO("BCG")),
                createAnnualRegistry()
        );
    }

    private CreatePatientDTO createInvalidRequest() {
        return new CreatePatientDTO(
                "",
                "Brasileira",
                LocalDate.now().plusDays(1),
                "8399999999",
                "",
                "Cartório X",
                "10",
                "A",
                "",
                LocalDate.of(2015, 1, 1),
                "SSP/PB",
                "",
                "123456789",
                "12345",
                LocalDate.now(),
                "",
                true,
                new CreateAddressDTO(
                        "Campina Grande",
                        "58000-000",
                        "PB",
                        "Centro",
                        "Rua X",
                        "123",
                        "Apt 1"
                ),
                new CreateGuardianDTO(
                        "Mãe",
                        "8399999999",
                        "MÃE",
                        new CreateAddressDTO(
                                "Campina Grande",
                                "58000-000",
                                "PB",
                                "Centro",
                                "Rua X",
                                "123",
                                "Apt 1"
                        )
                ),
                List.of(),
                Set.of(),
                createAnnualRegistry()
        );
    }

    @Test
    @DisplayName("Should create a new patient successfully (Returns 201)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreatePatientSuccessfully() throws Exception {

        CreatePatientDTO patientDTO = createRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.createPatient(patientDTO, docsDTO))
                .thenReturn(responseDTO);

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(patientDTO).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                        multipart("/patients")
                                .file(patientPart)
                                .file((MockMultipartFile) docsDTO.rg())
                                .file((MockMultipartFile) docsDTO.cpf())
                                .file((MockMultipartFile) docsDTO.proof_of_address())
                                .file((MockMultipartFile) docsDTO.birth_certificate())
                                .file((MockMultipartFile) docsDTO.photo())
                                .file((MockMultipartFile) docsDTO.reports().getFirst())
                                .file((MockMultipartFile) docsDTO.referrals().getFirst())
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.id().toString()))
                .andExpect(jsonPath("$.fullName").value("João da Silva"))
                .andExpect(jsonPath("$.birthplace").value("Campina Grande"))
                .andExpect(jsonPath("$.guardian.name").value("Mãe"))
                .andExpect(jsonPath("$.parents", hasSize(1)))
                .andExpect(jsonPath("$.vaccineNames", hasSize(1)))
                .andExpect(jsonPath("$.vaccineNames[0].name").value("BCG"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when providing invalid data")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {

        CreatePatientDTO invalidDTO = createInvalidRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(invalidDTO).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                        multipart("/patients")
                                .file(patientPart)
                                .file((MockMultipartFile) docsDTO.rg())
                                .file((MockMultipartFile) docsDTO.cpf())
                                .file((MockMultipartFile) docsDTO.proof_of_address())
                                .file((MockMultipartFile) docsDTO.birth_certificate())
                                .file((MockMultipartFile) docsDTO.photo())
                                .file((MockMultipartFile) docsDTO.reports().getFirst())
                                .file((MockMultipartFile) docsDTO.referrals().getFirst())
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 Conflict when patient already exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnConflictWhenPatientAlreadyExists() throws Exception {

        CreatePatientDTO patientDTO = createRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();

        when(patientService.createPatient(patientDTO, docsDTO))
                .thenThrow(new PatientConflictException());

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(patientDTO).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                        multipart("/patients")
                                .file(patientPart)
                                .file((MockMultipartFile) docsDTO.rg())
                                .file((MockMultipartFile) docsDTO.cpf())
                                .file((MockMultipartFile) docsDTO.proof_of_address())
                                .file((MockMultipartFile) docsDTO.birth_certificate())
                                .file((MockMultipartFile) docsDTO.photo())
                                .file((MockMultipartFile) docsDTO.reports().getFirst())
                                .file((MockMultipartFile) docsDTO.referrals().getFirst())
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return transtornos")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTranstornos() throws Exception {

        when(disorderApplicationService.findAllDisorders())
                .thenReturn(List.of(new DisorderResponseDTO(null, "TEA", true)));

        mockMvc.perform(get("/patients/filtros/transtornos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("TEA"));
    }

    @Test
    @DisplayName("Should return anos")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAnos() throws Exception {

        when(annualRegistryApplicationService.findAllRegistryYears())
                .thenReturn(List.of("2024", "2025"));

        mockMvc.perform(get("/patients/filtros/anos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should return cidades")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnCidades() throws Exception {

        when(patientService.findAllPatientCities())
                .thenReturn(List.of("Campina Grande"));

        mockMvc.perform(get("/patients/filtros/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Campina Grande"));
    }

    @Test
    @DisplayName("Should return tipos atendimento")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTiposAtendimento() throws Exception {

        when(serviceAreaApplicationService.findAllServiceTypes())
                .thenReturn(List.of(new ServiceTypeResponseDTO(null, "Psicologia")));

        mockMvc.perform(get("/patients/filtros/tipos-atendimento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Psicologia"));
    }
}

