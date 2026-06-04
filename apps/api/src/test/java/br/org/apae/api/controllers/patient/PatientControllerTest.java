package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.controllers.patient.mocks.patient.PatientCreator;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.exceptions.PatientExceptionHandler;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.domain.exceptions.PatientConflictException;
import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class,
        GlobalExceptionHandler.class,
        PatientExceptionHandler.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
public class PatientControllerTest {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {}

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

    private static final String BASE_URL = "/patients";

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    private CreateAddressDTO createAddress() {
        return new CreateAddressDTO("Campina Grande", "58000-000", "PB", "Centro", "Rua X", "123", "Apt 1");
    }

    private CreateAnnualRegistryDTO createAnnualRegistry() {
        return new CreateAnnualRegistryDTO(
                "123456789",
                "Nenhuma doença pré-existente",
                "Nenhum",
                new BigDecimal("1412.00"),
                Year.of(2024),
                Set.of(new CreateDisorderDTO("TEA")),
                Set.of(new CreateServiceAreaDTO("Psicologia"))
        );
    }

    private CreatePatientDTO createValidPatientRequest() {
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
                createAddress(),
                new CreateGuardianDTO("Mãe", "8399999999", "MÃE", createAddress()),
                List.of(new CreateParentDTO("Pai", "123456", "000.000.000-00", "Autônomo", true, "PAI")),
                Set.of(new CreateVaccineDTO("BCG")),
                createAnnualRegistry()
        );
    }

    private CreatePatientDTO createInvalidPatientRequest() {
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
                createAddress(),
                new CreateGuardianDTO("Mãe", "8399999999", "MÃE", createAddress()),
                List.of(),
                Set.of(),
                createAnnualRegistry()
        );
    }

    @Test
    @DisplayName("Deve criar um novo paciente com sucesso (201)")
    void shouldCreatePatientSuccessfully() throws Exception {
        CreatePatientDTO patientDTO = createValidPatientRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.createPatient(any(CreatePatientDTO.class), any(CreateDocumentsDTO.class)))
                .thenReturn(responseDTO);

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(patientDTO).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(
                        multipart(BASE_URL)
                                .file(patientPart)
                                .file((MockMultipartFile) docsDTO.rg())
                                .file((MockMultipartFile) docsDTO.cpf())
                                .file((MockMultipartFile) docsDTO.proof_of_address())
                                .file((MockMultipartFile) docsDTO.birth_certificate())
                                .file((MockMultipartFile) docsDTO.photo())
                                .file((MockMultipartFile) docsDTO.reports().getFirst())
                                .file((MockMultipartFile) docsDTO.referrals().getFirst())
                                .header("Authorization", AuthTestHelper.bearerToken())
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
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
    @DisplayName("Deve retornar 400 ao criar paciente com dados inválidos")
    void shouldReturnBadRequestWhenCreateDataIsInvalid() throws Exception {
        CreatePatientDTO invalidDTO = createInvalidPatientRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(invalidDTO).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(
                        multipart(BASE_URL)
                                .file(patientPart)
                                .file((MockMultipartFile) docsDTO.rg())
                                .file((MockMultipartFile) docsDTO.cpf())
                                .file((MockMultipartFile) docsDTO.proof_of_address())
                                .file((MockMultipartFile) docsDTO.birth_certificate())
                                .file((MockMultipartFile) docsDTO.photo())
                                .file((MockMultipartFile) docsDTO.reports().getFirst())
                                .file((MockMultipartFile) docsDTO.referrals().getFirst())
                                .header("Authorization", AuthTestHelper.bearerToken())
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 409 ao criar paciente já existente")
    void shouldReturnConflictWhenPatientAlreadyExists() throws Exception {
        CreatePatientDTO patientDTO = createValidPatientRequest();
        CreateDocumentsDTO docsDTO = PatientCreator.createDocuments();

        when(patientService.createPatient(any(CreatePatientDTO.class), any(CreateDocumentsDTO.class)))
                .thenThrow(new PatientConflictException());

        MockMultipartFile patientPart = new MockMultipartFile(
                "patient", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(patientDTO).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(
                        multipart(BASE_URL)
                                .file(patientPart)
                                .file((MockMultipartFile) docsDTO.rg())
                                .file((MockMultipartFile) docsDTO.cpf())
                                .file((MockMultipartFile) docsDTO.proof_of_address())
                                .file((MockMultipartFile) docsDTO.birth_certificate())
                                .file((MockMultipartFile) docsDTO.photo())
                                .file((MockMultipartFile) docsDTO.reports().getFirst())
                                .file((MockMultipartFile) docsDTO.referrals().getFirst())
                                .header("Authorization", AuthTestHelper.bearerToken())
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve buscar paciente por ID com sucesso (200)")
    void shouldRetrievePatientById() throws Exception {
        UUID id = UUID.randomUUID();
        PatientResponseDTO patientDTO = PatientCreator.createResponse();

        when(patientService.findPatientById(id)).thenReturn(patientDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
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
    @DisplayName("Deve retornar 404 ao buscar paciente com ID inexistente")
    void shouldReturnNotFoundWhenPatientIdDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.findPatientById(id)).thenThrow(new PatientNotFoundException());

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar todos os pacientes com paginação quando nenhum filtro for fornecido (200)")
    void shouldReturnAllPatientsWhenNoFilterProvided() throws Exception {
        PatientSummaryResponseDTO patient = PatientCreator.createSummaryResponse();
        Page<PatientSummaryResponseDTO> page = new PageImpl<>(List.of(patient), PageRequest.of(0, 10), 1);

        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(page);

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(patient.id().toString()))
                .andExpect(jsonPath("$.content[0].fullName").value("João da Silva"))
                .andExpect(jsonPath("$.content[0].birthplace").value("Campina Grande"))
                .andExpect(jsonPath("$.content[0].birthDate").value("2010-01-01"))
                .andExpect(jsonPath("$.content[0].contact").value("8399999999"))
                .andExpect(jsonPath("$.content[0].address.cep").value("58000-000"))
                .andExpect(jsonPath("$.content[0].address.city").value("Campina Grande"));
    }

    @Test
    @DisplayName("Deve repassar filtro 'name' ao serviço corretamente")
    void shouldPassNameFilterToService() throws Exception {
        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("name", "João")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(patientService).findPatientByFilter(filtersCaptor.capture(), any());
        assertEquals("João", filtersCaptor.getValue().get("name"));
    }

    @Test
    @DisplayName("Deve repassar filtro 'city' ao serviço corretamente")
    void shouldPassCityFilterToService() throws Exception {
        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("city", "Campina")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(patientService).findPatientByFilter(filtersCaptor.capture(), any());
        assertEquals("Campina", filtersCaptor.getValue().get("city"));
    }

    @Test
    @DisplayName("Deve repassar filtro 'disorder' ao serviço corretamente")
    void shouldPassDisorderFilterToService() throws Exception {
        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("disorder", "Autista")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(patientService).findPatientByFilter(filtersCaptor.capture(), any());
        assertEquals("Autista", filtersCaptor.getValue().get("disorder"));
    }

    @Test
    @DisplayName("Deve repassar filtro 'year' ao serviço corretamente")
    void shouldPassYearFilterToService() throws Exception {
        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("year", "2024")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(patientService).findPatientByFilter(filtersCaptor.capture(), any());
        assertEquals("2024", filtersCaptor.getValue().get("year"));
    }

    @Test
    @DisplayName("Deve repassar filtro 'treatmentType' ao serviço corretamente")
    void shouldPassTreatmentTypeFilterToService() throws Exception {
        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("treatmentType", "Fisioterapia")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(patientService).findPatientByFilter(filtersCaptor.capture(), any());
        assertEquals("Fisioterapia", filtersCaptor.getValue().get("treatmentType"));
    }

    @Test
    @DisplayName("Deve remover filtros em branco antes de repassar ao serviço")
    void shouldRemoveBlankFilters() throws Exception {
        when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("name", "   ")
                        .param("city", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> filtersCaptor = ArgumentCaptor.forClass(Map.class);
        verify(patientService).findPatientByFilter(filtersCaptor.capture(), any());
        assertEquals(0, filtersCaptor.getValue().size());
    }

    @Test
    @DisplayName("Deve retornar pacientes com faltas com sucesso (200)")
    void shouldReturnPatientsWithAbsences() throws Exception {
        when(patientService.findPatientsWithAbsences(any(), any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get(BASE_URL + "/with-absences")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("minAbsences", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve atualizar o paciente com sucesso (200)")
    void shouldUpdatePatientSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        var updateDTO = PatientCreator.createUpdatePayload();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.updatePatient(id, updateDTO)).thenReturn(responseDTO);

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
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
                .andExpect(jsonPath("$.allergies").value("Nenhuma"))
                .andExpect(jsonPath("$.isStudent").value(true))
                .andExpect(jsonPath("$.isDeleted").value(false))
                .andExpect(jsonPath("$.photoUrl").value("http://url-foto.com"))
                .andExpect(jsonPath("$.address.cep").value("58000-000"))
                .andExpect(jsonPath("$.address.city").value("Campina Grande"))
                .andExpect(jsonPath("$.guardian.name").value("Mãe"))
                .andExpect(jsonPath("$.parents", hasSize(1)))
                .andExpect(jsonPath("$.parents[0].name").value("Pai"))
                .andExpect(jsonPath("$.vaccineNames", hasSize(1)))
                .andExpect(jsonPath("$.vaccineNames[0].name").value("BCG"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao atualizar paciente com dados inválidos")
    void shouldReturnBadRequestWhenUpdateDataIsInvalid() throws Exception {
        mockMvc.perform(put(BASE_URL + "/{id}", UUID.randomUUID())
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .content(objectMapper.writeValueAsString(PatientCreator.createInvalidUpdateRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar um paciente inexistente")
    void shouldReturnNotFoundWhenUpdatingNonExistentPatient() throws Exception {
        UUID id = UUID.randomUUID();
        var updateDTO = PatientCreator.createUpdatePayload();

        when(patientService.updatePatient(id, updateDTO)).thenThrow(new PatientNotFoundException());

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar a foto do paciente com sucesso (200)")
    void shouldUpdatePatientPhotoSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();
        PatientResponseDTO responseDTO = PatientCreator.createResponse();

        when(patientService.updatePatientPhoto(eq(id), any())).thenReturn(responseDTO);

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", "img".getBytes());

        mockMvc.perform(multipart(BASE_URL + "/{id}/photo", id)
                        .file(photo)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.id().toString()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar foto de paciente inexistente")
    void shouldReturnNotFoundWhenUpdatingPhotoOfNonExistentPatient() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.updatePatientPhoto(eq(id), any())).thenThrow(new PatientNotFoundException());

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", "image/jpeg", "img".getBytes());

        mockMvc.perform(multipart(BASE_URL + "/{id}/photo", id)
                        .file(photo)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve realizar deleção lógica (inativar) com sucesso (204)")
    void shouldPerformLogicalDeletionSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(patientService).deletePatient(id);

        mockMvc.perform(patch(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar inativar ID inexistente")
    void shouldReturnNotFoundWhenDeletingNonExistentPatient() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new PatientNotFoundException()).when(patientService).deletePatient(id);

        mockMvc.perform(patch(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar transtornos disponíveis para filtro")
    void shouldListAvailableDisordersForFilter() throws Exception {
        when(disorderApplicationService.findAllDisorders()).thenReturn(List.of(
                new DisorderResponseDTO(UUID.randomUUID(), "TEA", true),
                new DisorderResponseDTO(UUID.randomUUID(), "TDAH", true)));

        mockMvc.perform(get(BASE_URL + "/filtros/transtornos")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("TEA"))
                .andExpect(jsonPath("$[1]").value("TDAH"));
    }

    @Test
    @DisplayName("Deve listar anos disponíveis para filtro")
    void shouldListAvailableYearsForFilter() throws Exception {
        when(annualRegistryApplicationService.findAllRegistryYears()).thenReturn(List.of("2024", "2025"));

        mockMvc.perform(get(BASE_URL + "/filtros/anos")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("2024"))
                .andExpect(jsonPath("$[1]").value("2025"));
    }

    @Test
    @DisplayName("Deve listar cidades disponíveis para filtro")
    void shouldListAvailableCitiesForFilter() throws Exception {
        when(patientService.findAllPatientCities()).thenReturn(List.of("Campina Grande", "Esperança"));

        mockMvc.perform(get(BASE_URL + "/filtros/cidades")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Campina Grande"))
                .andExpect(jsonPath("$[1]").value("Esperança"));
    }

    @Test
    @DisplayName("Deve listar tipos de atendimento disponíveis para filtro")
    void shouldListAvailableServiceAreas() throws Exception {
        when(serviceAreaApplicationService.findAllServiceAreas()).thenReturn(List.of(
                new ServiceAreaResponseDTO(1, "Fisioterapia"),
                new ServiceAreaResponseDTO(2, "Nutrição")));

        mockMvc.perform(get(BASE_URL + "/filtros/tipos-atendimento")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Fisioterapia"))
                .andExpect(jsonPath("$[1]").value("Nutrição"));
    }
}
