package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.domain.exceptions.PatientConflictException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.controllers.patient.mocks.patient.PatientCreator;
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

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    private ServiceAreaApplicationService serviceAreaApplicationService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should create a new patient successfully (Returns 201)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreatePatientSuccessfully() throws Exception {
        CreatePatientDTO patientDTO = PatientCreator.createRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.createPatient(patientDTO, docsDTO))
                .thenReturn(responseDTO);

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient",
                "",
                "application/json",
                objectMapper.writeValueAsString(patientDTO).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/patients")
                        .file(patientPart)
                        .file((MockMultipartFile) docsDTO.rg())
                        .file((MockMultipartFile) docsDTO.cpf())
                        .file((MockMultipartFile) docsDTO.proof_of_address())
                        .file((MockMultipartFile) docsDTO.birth_certificate())
                        .file((MockMultipartFile) docsDTO.photo())
                        .file((MockMultipartFile) docsDTO.reports().getFirst())
                        .file((MockMultipartFile) docsDTO.referrals().getFirst())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.id().toString()))
                .andExpect(jsonPath("$.fullName").value("João da Silva"))
                .andExpect(jsonPath("$.birthplace").value("Campina Grande"))
                .andExpect(jsonPath("$.birthDate").value("2010-01-01"))
                .andExpect(jsonPath("$.contact").value("8399999999"))
                .andExpect(jsonPath("$.birthCertificateNumber").value("123456"))
                .andExpect(jsonPath("$.registryOffice").value("Cartório X"))
                .andExpect(jsonPath("$.fls").value("10"))
                .andExpect(jsonPath("$.book").value("A"))
                .andExpect(jsonPath("$.rg").value("123456"))
                .andExpect(jsonPath("$.issueDate").value("2015-01-01"))
                .andExpect(jsonPath("$.issuingAgency").value("SSP/PB"))
                .andExpect(jsonPath("$.cpf").value("000.000.000-00"))
                .andExpect(jsonPath("$.cns").value("123456789"))
                .andExpect(jsonPath("$.nis").value("12345"))
                .andExpect(jsonPath("$.registrationDate").value(responseDTO.registrationDate().toString()))
                .andExpect(jsonPath("$.allergies").value("Nenhuma"))
                .andExpect(jsonPath("$.isStudent").value(true))
                .andExpect(jsonPath("$.isDeleted").value(false))
                .andExpect(jsonPath("$.photoUrl").value("http://url-foto.com"))
                .andExpect(jsonPath("$.address.cep").value("58000-000"))
                .andExpect(jsonPath("$.address.city").value("Campina Grande"))
                .andExpect(jsonPath("$.address.state").value("PB"))
                .andExpect(jsonPath("$.address.neighborhood").value("Centro"))
                .andExpect(jsonPath("$.address.street").value("Rua X"))
                .andExpect(jsonPath("$.address.number").value("123"))
                .andExpect(jsonPath("$.address.complement").value("Apt 1"))
                .andExpect(jsonPath("$.guardian.name").value("Mãe"))
                .andExpect(jsonPath("$.guardian.contact").value("8399999999"))
                .andExpect(jsonPath("$.guardian.kinship").value("MÃE"))
                .andExpect(jsonPath("$.guardian.address.city").value("Campina Grande"))
                .andExpect(jsonPath("$.parents", hasSize(1)))
                .andExpect(jsonPath("$.parents[0].name").value("Pai"))
                .andExpect(jsonPath("$.parents[0].rg").value("123456"))
                .andExpect(jsonPath("$.parents[0].cpf").value("000.000.000-00"))
                .andExpect(jsonPath("$.parents[0].profession").value("Autônomo"))
                .andExpect(jsonPath("$.parents[0].kinship").value("PAI"))
                .andExpect(jsonPath("$.parents[0].isAlive").value(true))
                .andExpect(jsonPath("$.vaccineNames", hasSize(1)))
                .andExpect(jsonPath("$.vaccineNames[0].name").value("BCG"));
    }


    @Test
    @DisplayName("Should return 400 Bad Request when providing invalid data")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnBadRequestWhenDataIsInvalid() throws Exception {
        CreatePatientDTO invalidDTO = PatientCreator.createInvalidRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient",
                "",
                "application/json",
                objectMapper.writeValueAsString(invalidDTO).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/patients")
                        .file(patientPart)
                        .file((MockMultipartFile) docsDTO.rg())
                        .file((MockMultipartFile) docsDTO.cpf())
                        .file((MockMultipartFile) docsDTO.proof_of_address())
                        .file((MockMultipartFile) docsDTO.birth_certificate())
                        .file((MockMultipartFile) docsDTO.photo())
                        .file((MockMultipartFile) docsDTO.reports().getFirst())
                        .file((MockMultipartFile) docsDTO.referrals().getFirst())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 Conflict when patient already exists")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnConflictWhenPatientAlreadyExists() throws Exception {
        CreatePatientDTO patientDTO = PatientCreator.createRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();

        when(patientService.createPatient(patientDTO,docsDTO))
                .thenThrow(new PatientConflictException());

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient",
                "",
                "application/json",
                objectMapper.writeValueAsString(patientDTO).getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/patients")
                        .file(patientPart)
                        .file((MockMultipartFile) docsDTO.rg())
                        .file((MockMultipartFile) docsDTO.cpf())
                        .file((MockMultipartFile) docsDTO.proof_of_address())
                        .file((MockMultipartFile) docsDTO.birth_certificate())
                        .file((MockMultipartFile) docsDTO.photo())
                        .file((MockMultipartFile) docsDTO.reports().getFirst())
                        .file((MockMultipartFile) docsDTO.referrals().getFirst())
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isConflict());
    }
}
