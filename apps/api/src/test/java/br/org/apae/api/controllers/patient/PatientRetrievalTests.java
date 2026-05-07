/*package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.controllers.patient.mocks.patient.PatientCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("patient")
})
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

    @Captor
    private ArgumentCaptor<Map<String, String>> filtersCaptor;

    @Test
    @DisplayName("Should retrieve patient by ID successfully (Returns 200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldRetrievePatientById() throws Exception {
        UUID randomId = UUID.randomUUID();
        PatientResponseDTO patientDTO = PatientCreator.createResponse();

        when(patientService.findPatientById(randomId))
                .thenReturn(patientDTO);

        mockMvc.perform(get("/patients/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDTO.id().toString()))
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
                .andExpect(jsonPath("$.registrationDate").value(patientDTO.registrationDate().toString()))
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
    @DisplayName("Should return 404 Not Found when patient ID does not exist")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenPatientIdDoesNotExist() throws Exception {
        UUID randomId = UUID.randomUUID();

        when(patientService.findPatientById(randomId))
                .thenThrow(PatientNotFoundException.class);

        mockMvc.perform(get("/patients/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return all patients (no filters provided)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllPatientsWhenNoFilterProvided() throws Exception {
        List<PatientSummaryResponseDTO> list = List.of(PatientCreator.createSummaryResponse());
        when(patientService.findPatientByFilter(anyMap())).thenReturn(list);

        mockMvc.perform(get("/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(list.getFirst().id().toString()))
                .andExpect(jsonPath("$[0].fullName").value("João da Silva"))
                .andExpect(jsonPath("$[0].birthplace").value("Campina Grande"))
                .andExpect(jsonPath("$[0].birthDate").value("2010-01-01"))
                .andExpect(jsonPath("$[0].contact").value("8399999999"))
                .andExpect(jsonPath("$[0].birthCertificateNumber").value("123456"))
                .andExpect(jsonPath("$[0].registryOffice").value("Cartório X"))
                .andExpect(jsonPath("$[0].fls").value("10"))
                .andExpect(jsonPath("$[0].book").value("A"))
                .andExpect(jsonPath("$[0].rg").value("123456"))
                .andExpect(jsonPath("$[0].issueDate").value("2015-01-01"))
                .andExpect(jsonPath("$[0].issuingAgency").value("SSP/PB"))
                .andExpect(jsonPath("$[0].cpf").value("000.000.000-00"))
                .andExpect(jsonPath("$[0].cns").value("123456789"))
                .andExpect(jsonPath("$[0].nis").value("12345"))
                .andExpect(jsonPath("$[0].registrationDate").value(list.getFirst().registrationDate().toString()))
                .andExpect(jsonPath("$[0].allergies").value("Nenhuma"))
                .andExpect(jsonPath("$[0].isStudent").value(true))
                .andExpect(jsonPath("$[0].isDeleted").value(false))
                .andExpect(jsonPath("$[0].photoUrl").value("http://url-foto.com"))
                .andExpect(jsonPath("$[0].address.cep").value("58000-000"))
                .andExpect(jsonPath("$[0].address.city").value("Campina Grande"))
                .andExpect(jsonPath("$[0].address.state").value("PB"))
                .andExpect(jsonPath("$[0].address.neighborhood").value("Centro"))
                .andExpect(jsonPath("$[0].address.street").value("Rua X"))
                .andExpect(jsonPath("$[0].address.number").value("123"))
                .andExpect(jsonPath("$[0].address.complement").value("Apt 1"));
    }

    @Test
    @DisplayName("Should correctly pass 'name' filter to service")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldPassNameFilterToService() throws Exception {
        String nomeBusca = "João";
        when(patientService.findPatientByFilter(anyMap())).thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("name", nomeBusca)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).findPatientByFilter(filtersCaptor.capture());
        Map<String, String> filtrosCapturados = filtersCaptor.getValue();

        assertEquals(nomeBusca, filtrosCapturados.get("name"));
    }

    @Test
    @DisplayName("Should correctly pass 'city' filter to service")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldPassCityFilterToService() throws Exception {
        String cidade = "Campina";
        when(patientService.findPatientByFilter(anyMap())).thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("city", cidade)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).findPatientByFilter(filtersCaptor.capture());
        assertEquals(cidade, filtersCaptor.getValue().get("city"));
    }

    @Test
    @DisplayName("Should correctly pass 'disorder' filter to service")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldPassDisorderFilterToService() throws Exception {
        String transtorno = "Autista";
        when(patientService.findPatientByFilter(anyMap())).thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("disorder", transtorno)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).findPatientByFilter(filtersCaptor.capture());
        assertEquals(transtorno, filtersCaptor.getValue().get("disorder"));
    }

    @Test
    @DisplayName("Should correctly pass 'year' filter to service")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldPassYearFilterToService() throws Exception {
        String ano = "2024";
        when(patientService.findPatientByFilter(anyMap())).thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("year", ano)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).findPatientByFilter(filtersCaptor.capture());
        assertEquals(ano, filtersCaptor.getValue().get("year"));
    }

    @Test
    @DisplayName("Should correctly pass 'treatmentType' filter to service")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldPassTreatmentTypeFilterToService() throws Exception {
        String tipo = "Fisioterapia";
        when(patientService.findPatientByFilter(anyMap())).thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("treatmentType", tipo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).findPatientByFilter(filtersCaptor.capture());
        assertEquals(tipo, filtersCaptor.getValue().get("treatmentType"));
    }

    @Test
    @DisplayName("Should list available disorder names for filtering")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldListAvailableDisordersForFilter() throws Exception {
        DisorderResponseDTO tea = new DisorderResponseDTO(UUID.randomUUID(), "TEA");
        DisorderResponseDTO tdah = new DisorderResponseDTO(UUID.randomUUID(), "TDAH");

        List<DisorderResponseDTO> listaDoService = List.of(tea, tdah);

        when(disorderApplicationService.findAllDisorders()).thenReturn(listaDoService);

        mockMvc.perform(get("/patients/filtros/disorders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("TEA"))
                .andExpect(jsonPath("$[1]").value("TDAH"));
    }

    @Test
    @DisplayName("Should list available years for filtering")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldListAvailableYearsForFilter() throws Exception {
        List<String> mockData = List.of("2024", "2025");
        when(annualRegistryApplicationService.findAllRegistryYears()).thenReturn(mockData);

        mockMvc.perform(get("/patients/filtros/anos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("2024"))
                .andExpect(jsonPath("$[1]").value("2025"));
    }

    @Test
    @DisplayName("Should list available cities for filtering")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldListAvailableCitiesForFilter() throws Exception {
        List<String> mockData = List.of("Campina Grande", "Esperança");
        when(patientService.findAllPatientCities()).thenReturn(mockData);

        mockMvc.perform(get("/patients/filtros/cidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Campina Grande"))
                .andExpect(jsonPath("$[1]").value("Esperança"));
    }

    @Test
    @DisplayName("Should list available service areas ")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldListAvailableAllServiceArea() throws Exception {
        List<ServiceAreaResponseDTO> mockData = List.of(new ServiceAreaResponseDTO(1, "Fisioterapia"), new ServiceAreaResponseDTO(2, "Nutrição"));
        when(serviceAreaApplicationService.findAllServiceAreas()).thenReturn(mockData);

        mockMvc.perform(get("/patients/filtros/tipos-atendimento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Fisioterapia"))
                .andExpect(jsonPath("$[1]").value("Nutrição"));
    }

    @Test
    @DisplayName("Should remove blank filters before passing to service")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldRemoveBlankFilters() throws Exception {
        when(patientService.findPatientByFilter(anyMap()))
                .thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("name", "   ")
                        .param("city", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).findPatientByFilter(filtersCaptor.capture());

        Map<String, String> filtrosCapturados = filtersCaptor.getValue();

        assertEquals(0, filtrosCapturados.size());
    }
}
    */
