package br.org.apae.api.controllers.annual_registry;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.exceptions.PatientExceptionHandler;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.domain.exceptions.AnnualRegistryConflictException;
import br.org.apae.api.patient.domain.exceptions.RegistryNotFoundException;
import br.org.apae.api.patient.domain.exceptions.RegistryOwnershipException;
import br.org.apae.api.patient.domain.exceptions.DisorderMismatchException;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaNotFoundException;
import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AnnualRegistryControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class,
        PatientExceptionHandler.class,
        GlobalExceptionHandler.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
public class AnnualRegistryControllerTest {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AnnualRegistryApplicationService annualRegistryService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private static final String BASE_URL = "/patients/{id}/annual-registry";

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    @Test
    @DisplayName("Deve criar registro com sucesso (201) quando todos dados e dependências (Patient, ServiceArea, Disorders) são válidos")
    void shouldCreateRegistrySuccess() throws Exception {
        UUID patientId = UUID.randomUUID();
        CreateAnnualRegistryDTO requestDto = createValidCreateDTO();
        AnnualRegistryResponseDTO responseDto = createResponseDTO(patientId, requestDto.year().getValue());

        when(annualRegistryService.createRegistry(requestDto, patientId))
                .thenReturn(responseDto);

        mockMvc.perform(post(BASE_URL, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.year").value(requestDto.year().getValue()))
                .andExpect(jsonPath("$.patientId").value(patientId.toString()));
    }

    @Test
    @DisplayName("Deve retornar Erro de Validação quando campos obrigatórios do DTO são nulos")
    void shouldReturnBadRequestWhenMandatoryFieldsAreNull() throws Exception {
        UUID patientId = UUID.randomUUID();
        CreateAnnualRegistryDTO invalidDto = new CreateAnnualRegistryDTO(
                null,
                null,
                "Meds",
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post(BASE_URL, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar NotFound quando o Paciente não existe na criação")
    void shouldReturnNotFoundWhenPatientDoesNotExistOnCreate() throws Exception {
        UUID patientId = UUID.randomUUID();
        CreateAnnualRegistryDTO requestDto = createValidCreateDTO();

        when(annualRegistryService.createRegistry(requestDto,patientId))
                .thenThrow(new PatientNotFoundException());

        mockMvc.perform(post(BASE_URL, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 404 quando Disorders enviadas não existem ou não batem")
    void shouldReturnErrorWhenDisorderMismatch() throws Exception {
        UUID patientId = UUID.randomUUID();
        CreateAnnualRegistryDTO requestDto = createValidCreateDTO();

        when(annualRegistryService.createRegistry(requestDto, patientId))
                .thenThrow(new DisorderMismatchException());

        mockMvc.perform(post(BASE_URL, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar NotFound quando ServiceArea enviada não é encontrada")
    void shouldReturnErrorWhenServiceAreaNotFound() throws Exception {
        UUID patientId = UUID.randomUUID();
        CreateAnnualRegistryDTO requestDto = createValidCreateDTO();

        when(annualRegistryService.createRegistry(any(), eq(patientId)))
                .thenThrow(new ServiceAreaNotFoundException());

        mockMvc.perform(post(BASE_URL, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Conflict ao tentar criar registro para ano já existente")
    void shouldReturnConflictWhenYearAlreadyExists() throws Exception {
        UUID patientId = UUID.randomUUID();
        CreateAnnualRegistryDTO requestDto = createValidCreateDTO();

        when(annualRegistryService.createRegistry(requestDto, patientId))
                .thenThrow(new AnnualRegistryConflictException(requestDto.year()));

        mockMvc.perform(post(BASE_URL, patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve buscar registro por ano com sucesso")
    void shouldGetRegistryByYearSuccess() throws Exception {
        UUID patientId = UUID.randomUUID();
        Integer year = 2024;
        AnnualRegistryResponseDTO responseDto = createResponseDTO(patientId, year);

        when(annualRegistryService.findRegistryByPatientAndYear(patientId, Year.of(year)))
                .thenReturn(responseDto);

        mockMvc.perform(get(BASE_URL + "/{year}", patientId, year)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(year));
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) se registro para o ano não existir")
    void shouldReturnNotFoundWhenRegistryByYearDoesNotExist() throws Exception {
        UUID patientId = UUID.randomUUID();
        Integer year = 2025;

        when(annualRegistryService.findRegistryByPatientAndYear(patientId, Year.of(year)))
                .thenThrow(new RegistryNotFoundException(Year.of(year)));

        mockMvc.perform(get(BASE_URL + "/{year}", patientId, year)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) se paciente não existir na busca")
    void shouldReturnNotFoundWhenPatientDoesNotExistOnGet() throws Exception {
        UUID patientId = UUID.randomUUID();
        Integer year = 2024;

        when(annualRegistryService.findRegistryByPatientAndYear(patientId, Year.of(year)))
                .thenThrow(new PatientNotFoundException());

        mockMvc.perform(get(BASE_URL + "/{year}", patientId, year)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());
    }
/*
    @Test
    @DisplayName("PATCH - Deve atualizar parcialmente registro com sucesso")
    void shouldUpdateRegistrySuccess() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID registryId = UUID.randomUUID();
        UpdateAnnualRegistryDTO updateDto = new UpdateAnnualRegistryDTO(2025);
        AnnualRegistryResponseDTO responseDto = createResponseDTO(patientId, 2025);

        when(annualRegistryService.updateRegistry(patientId, registryId, updateDto))
                .thenReturn(responseDto);

        mockMvc.perform(patch(BASE_URL + "/{registryId}", patientId, registryId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2025));
    }

    @Test
    @DisplayName("PATCH - Deve retornar Conflict (409) se atualização causar duplicidade de ano")
    void shouldReturnConflictOnUpdateWithExistingYear() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID registryId = UUID.randomUUID();
        UpdateAnnualRegistryDTO updateDto = new UpdateAnnualRegistryDTO(2025);

        when(annualRegistryService.updateRegistry(patientId, registryId, updateDto))
                .thenThrow(new AnnualRegistryConflictException(2025));

        mockMvc.perform(patch(BASE_URL + "/{registryId}", patientId, registryId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isConflict());
    }
*/
    @Test
    @DisplayName("PUT - Deve substituir registro totalmente com sucesso")
    void shouldReplaceRegistrySuccess() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID registryId = UUID.randomUUID();
        ReplaceAnnualRegistryDTO replaceDto = createValidReplaceDTO();
        AnnualRegistryResponseDTO responseDto = createResponseDTO(patientId, 2024);

        when(annualRegistryService.replaceRegistry(patientId, registryId, replaceDto))
                .thenReturn(responseDto);

        mockMvc.perform(put(BASE_URL + "/{registryId}", patientId, registryId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replaceDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT - Deve retornar NotFound (404) se tentar substituir registro que não pertence ao paciente (Ownership)")
    void shouldReturnNotFoundWhenRegistryOwnershipFail() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID registryId = UUID.randomUUID();
        ReplaceAnnualRegistryDTO replaceDto = createValidReplaceDTO();

        when(annualRegistryService.replaceRegistry(any(), any(), any()))
                .thenThrow(new RegistryOwnershipException(patientId, registryId));

        mockMvc.perform(put(BASE_URL + "/{registryId}", patientId, registryId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replaceDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar registro com sucesso (204)")
    void shouldDeleteRegistrySuccess() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID registryId = UUID.randomUUID();

        doNothing().when(annualRegistryService).deleteRegistry(patientId, registryId);

        mockMvc.perform(delete(BASE_URL + "/{registryId}", patientId, registryId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        verify(annualRegistryService).deleteRegistry(patientId, registryId);
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao deletar registro inexistente")
    void shouldReturnNotFoundWhenDeleteNonExistent() throws Exception {
        UUID patientId = UUID.randomUUID();
        UUID registryId = UUID.randomUUID();

        doThrow(new RegistryNotFoundException(registryId))
                .when(annualRegistryService).deleteRegistry(patientId, registryId);

        mockMvc.perform(delete(BASE_URL + "/{registryId}", patientId, registryId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar os anos de registro de um paciente com sucesso")
    void shouldGetRegistryYearsSuccess() throws Exception {
        UUID patientId = UUID.randomUUID();
        List<Integer> expectedYears = List.of(2022, 2023, 2024);
        when(annualRegistryService.listYearsByPatient(patientId))
                .thenReturn(expectedYears);
        mockMvc.perform(get(BASE_URL + "/years", patientId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value(2022))
                .andExpect(jsonPath("$[1]").value(2023))
                .andExpect(jsonPath("$[2]").value(2024));
        verify(annualRegistryService).listYearsByPatient(patientId);
    }

    private CreateAnnualRegistryDTO createValidCreateDTO() {
        return new CreateAnnualRegistryDTO(
                "123456",
                "Doença Teste",
                "Nenhum",
                BigDecimal.valueOf(2000.00),
                Year.of(2024),
                Collections.emptySet(),
                Collections.emptySet()
        );
    }

    private ReplaceAnnualRegistryDTO createValidReplaceDTO() {
        return new ReplaceAnnualRegistryDTO(
                "987654",
                "Nova Doença",
                BigDecimal.valueOf(3000.00),
                "Novos Meds",
                Collections.emptySet(),
                Collections.emptySet()
        );
    }

    private AnnualRegistryResponseDTO createResponseDTO(UUID patientId, Integer year) {
        return new AnnualRegistryResponseDTO(
                UUID.randomUUID(),
                "123456",
                "Doença X",
                "Remedio Y",
                BigDecimal.valueOf(1500),
                year,
                patientId,
                new HashSet<>(),
                new HashSet<>()
        );
    }
}
