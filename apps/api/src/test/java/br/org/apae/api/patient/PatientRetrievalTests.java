package br.org.apae.api.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.controllers.patient.PatientControllerImpl;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.utils.PatientCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientControllerImpl.class)
public class PatientRetrievalTests {
    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Deve buscar um paciente pelo ID com sucesso (Retorna 200)")
    @WithMockUser(username = "admin", roles = "admin")
    void deveBuscarPacientePorId() throws Exception {
        UUID randomId = UUID.randomUUID();
        PatientResponseDTO patientDTO = PatientCreator.createResponse();

        when(patientService.findPatientById(randomId))
                .thenReturn(patientDTO);

        mockMvc.perform(get("/patients/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fullName").value("João da Silva"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar paciente pelo ID inexistente")
    @WithMockUser(username = "admin", roles = "admin")
    void deveRetornarErroComIdInexistente() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(patientService.findPatientById(randomId))
                .thenThrow(PatientNotFoundException.class);

        mockMvc.perform(get("/patients/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar todos os pacientes com sucesso (Retorna 200)")
    @WithMockUser(username = "admin", roles = "admin")
    void deveRetornarTodosOsPacientes() throws Exception {
        Map<String, String> filters = new HashMap<>();
        List<PatientSummaryResponseDTO> patientDTO = List.of(PatientCreator.createSummaryResponse());

        when(patientService.findPatientByFilter(filters))
                .thenReturn(patientDTO);

        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].fullName").value("João da Silva"));
    }

    //TODO: Esperando o merge das branchs para validar os testes

//    @Test
//    @DisplayName("Deve retornar todos os paciente com o filtro name aplicado (Retorna 200)")
//    @WithMockUser(username = "admin", roles = "admin")
//    void deveRetornarTodosOsPacientesComOFiltroNome() throws Exception {
//        String param = "name";
//        String value = "João";
//        List<PatientSummaryResponseDTO> patientDTO = List.of(PatientCreator.createSummaryResponse());
//
//        when(patientService.findPatientByFilter(anyMap()))
//                .thenReturn(patientDTO);
//
//        mockMvc.perform(get("/patients")
//                        .param(param, value)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").exists())
//                .andExpect(jsonPath("$.[0].fullName").value("João da Silva"));
//    }
//
//    @Test
//    @DisplayName("Deve retornar todos os paciente com o filtro city aplicado (Retorna 200)")
//    @WithMockUser(username = "admin", roles = "admin")
//    void deveRetornarTodosOsPacientesComOFiltroCidade() throws Exception {
//        String param = "city";
//        String value = "Campina";
//        List<PatientSummaryResponseDTO> patientDTO = List.of(PatientCreator.createSummaryResponse());
//
//        when(patientService.findPatientByFilter(anyMap()))
//                .thenReturn(patientDTO);
//
//        mockMvc.perform(get("/patients")
//                        .param(param, value)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").exists())
//                .andExpect(jsonPath("$.[0].address.city").value("Campina Grande"));
//    }
//
//    @Test
//    @DisplayName("Deve retornar todos os paciente com o filtro disorder aplicado (Retorna 200)")
//    @WithMockUser(username = "admin", roles = "admin")
//    void deveRetornarTodosOsPacientesComOFiltroTranstorno() throws Exception {
//        String param = "disorder";
//        String value = "Autista";
//        List<PatientSummaryResponseDTO> patientDTO = List.of(PatientCreator.createSummaryResponse());
//
//        when(patientService.findPatientByFilter(anyMap()))
//                .thenReturn(patientDTO);
//
//        mockMvc.perform(get("/patients")
//                        .param(param, value)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id").exists())
//                .andExpect(jsonPath("$.[0].address.city").value("Campina Grande"));
//    }
//
//    @Test
//    @DisplayName("Deve retornar pacientes com o filtro year (Ano) aplicado (Retorna 200)")
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void deveRetornarTodosOsPacientesComOFiltroAno() throws Exception {
//        String param = "year";
//        String value = String.valueOf(LocalDate.now()).substring(0, 4);
//
//        List<PatientSummaryResponseDTO> patientDTO = List.of(PatientCreator.createSummaryResponse());
//
//        when(patientService.findPatientByFilter(anyMap()))
//                .thenReturn(patientDTO);
//
//        String currentDate = String.valueOf(LocalDate.now());
//
//        mockMvc.perform(get("/patients")
//                        .param(param, value)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").exists())
//                .andExpect(jsonPath("$[0].registrationDate").value(currentDate));
//    }
//
//    @Test
//    @DisplayName("Deve retornar pacientes com o filtro treatmentType (Tipo de Atendimento) (Retorna 200)")
//    @WithMockUser(username = "admin", roles = "admin")
//    void deveRetornarTodosOsPacientesComOFiltroTipoAtendimento() throws Exception {
//        String param = "treatmentType";
//        String value = "Fisioterapia";
//
//        List<PatientSummaryResponseDTO> patientDTO = List.of(PatientCreator.createSummaryResponse());
//
//        when(patientService.findPatientByFilter(anyMap()))
//                .thenReturn(patientDTO);
//
//        mockMvc.perform(get("/patients")
//                        .param(param, value)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").exists());
//    }
}
