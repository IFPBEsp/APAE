package br.org.apae.api.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientControllerImpl.class)
public class PatientModificationTests {
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
    @DisplayName("Deve atualizar um paciente com sucesso (Retorna 200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarPacienteComSucesso() throws Exception {
        UUID patientId = UUID.randomUUID();
        UpdatePatientDTO updateDTO = PatientCreator.createUpdatePayload();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.updatePatient(eq(patientId), any(UpdatePatientDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/patients/{id}", patientId)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName").value("João da Silva"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao enviar dados inválidos no Update")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveFalharValidacaoUpdate() throws Exception {
        UpdatePatientDTO invalidDTO = PatientCreator.createInvalidUpdateRequest();

        mockMvc.perform(put("/patients/{id}", UUID.randomUUID())
                        .content(objectMapper.writeValueAsString(invalidDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}

