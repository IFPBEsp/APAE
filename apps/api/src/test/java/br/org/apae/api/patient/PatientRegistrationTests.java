package br.org.apae.api.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.controllers.patient.PatientControllerImpl;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.utils.PatientCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Cria um novo paciente no sistema com todos os seus dados. (Retorna 201)")
    @WithMockUser(username = "admin", roles = {"admin"})
    void deveCriarUmPacienteComSucesso() throws Exception {
        CreatePatientDTO patientDTO = PatientCreator.createRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.createPatient(any(CreatePatientDTO.class), any(CreateDocumentsDTO.class)))
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
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName").value("João da Silva"));
    }


    @Test
    @DisplayName("Deve retornar 400 Bad Request quando enviar dados inválidos")
    @WithMockUser(username = "admin", roles = {"admin"})
    void deveRetornarErroComDadosInvalidos() throws Exception {
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
}
