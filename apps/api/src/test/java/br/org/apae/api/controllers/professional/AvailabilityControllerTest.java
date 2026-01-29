package br.org.apae.api.controllers.professional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.exceptions.AvailabilityExceptionHandler;
import br.org.apae.api.professional.application.interfaces.AvailabilityApplicationService;
import br.org.apae.api.controllers.mocks.professional.AvailabilityMockDto;
import br.org.apae.api.professional.domain.exceptions.AvailabilityConflictException;
import br.org.apae.api.professional.domain.exceptions.AvailabilityNotFoundException;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;

@Tag("controller")
@Tag("health-professional")
@Tag("availability")
@WebMvcTest(controllers = AvailabilityControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
    SecurityConfiguration.class,
    AvailabilityExceptionHandler.class
})
class AvailabilityControllerTest {

    private static final UUID PROFESSIONAL_ID = AvailabilityMockDto.PROFESSIONAL_ID_1;
    private static final UUID AVAILABILITY_ID = AvailabilityMockDto.AVAILABILITY_ID_1;

    private static final String URI =
            "/professionals/{professionalId}/availabilities";

    private static final String URI_WITH_ID =
            URI + "/{availabilityId}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AvailabilityApplicationService service;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private String bearer() {
        return AuthTestHelper.bearerToken();
    }

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    @Test
    @DisplayName("Cria com sucesso uma nova disponibilidade para um profissional")
    void shouldCreateAvailabilitySuccessfully() throws Exception {
        var request = AvailabilityMockDto.createAvailabilityRequestMorning();
        var response = AvailabilityMockDto.availabilityResponseMorning();

        Mockito.when(service.createAvailability(
                Mockito.eq(PROFESSIONAL_ID),
                any()))
                .thenReturn(response);

        mockMvc.perform(post(URI, PROFESSIONAL_ID)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id().toString()))
                .andExpect(jsonPath("$.day").value("SEGUNDA"))
                .andExpect(jsonPath("$.shift").value("MANHA"));

        Mockito.verify(service)
                .createAvailability(Mockito.eq(PROFESSIONAL_ID), any());
    }

    @Test
    @DisplayName("Retorna BadRequestException ao tentar criar uma disponibilidade inválida")
    void shouldReturnBadRequestWhenCreateInvalidBody() throws Exception {
        var invalid = new CreateAvailabilityDTO("", "");

        mockMvc.perform(post(URI, PROFESSIONAL_ID)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).createAvailability(any(), any());
    }

    @Test
    @DisplayName("Retorna ConflictException ao tentar criar disponibilidade com dia/turno já cadastrados para o mesmo profissional")
    void shouldReturnConflictWhenAvailabilityAlreadyExistsOnCreate() throws Exception {
        var request = AvailabilityMockDto.createAvailabilityRequestMorning();

        Mockito.when(service.createAvailability(Mockito.eq(PROFESSIONAL_ID), any()))
                .thenThrow(new AvailabilityConflictException());

        mockMvc.perform(post(URI, PROFESSIONAL_ID)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Retorna todas as disponibilidades cadastradas do profissional")
    void shouldReturnAllAvailabilities() throws Exception {
        var responses = AvailabilityMockDto.availabilityResponseList();

        Mockito.when(service.findAllByProfessional(PROFESSIONAL_ID))
                .thenReturn(responses);

        mockMvc.perform(get(URI, PROFESSIONAL_ID)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].day").value("SEGUNDA"))
                .andExpect(jsonPath("$[0].shift").value("MANHA"))
                .andExpect(jsonPath("$[1].day").value("TERCA"))
                .andExpect(jsonPath("$[1].shift").value("TARDE"));

        Mockito.verify(service).findAllByProfessional(PROFESSIONAL_ID);
    }

    @Test
    @DisplayName("Retorna uma lista vazia quando o profissional não possui disponibilidades")
    void shouldReturnEmptyListWhenNoAvailabilitiesExist() throws Exception {
        Mockito.when(service.findAllByProfessional(PROFESSIONAL_ID))
                .thenReturn(List.of());

        mockMvc.perform(get(URI, PROFESSIONAL_ID)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        Mockito.verify(service).findAllByProfessional(PROFESSIONAL_ID);
    }

    @Test
    @DisplayName("Atualiza com sucesso os dados de uma disponibilidade")
    void shouldUpdateAvailabilitySuccessfully() throws Exception {
        var request = AvailabilityMockDto.updateAvailabilityRequest();
        var updatedResponse = AvailabilityMockDto.availabilityResponseUpdated();

        Mockito.when(service.updateAvailability(
                Mockito.eq(PROFESSIONAL_ID),
                Mockito.eq(AVAILABILITY_ID),
                any()))
                .thenReturn(updatedResponse);

        mockMvc.perform(put(URI_WITH_ID, PROFESSIONAL_ID, AVAILABILITY_ID)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedResponse.id().toString()))
                .andExpect(jsonPath("$.day").value("QUARTA"))
                .andExpect(jsonPath("$.shift").value("TARDE"));

        Mockito.verify(service)
                .updateAvailability(Mockito.eq(PROFESSIONAL_ID), Mockito.eq(AVAILABILITY_ID), any());
    }

    @Test
    @DisplayName("Retorna NotFoundException ao tentar atualizar uma disponibilidade inexistente")
    void shouldReturnNotFoundWhenUpdateNonExistingAvailability() throws Exception {
        var request = AvailabilityMockDto.updateAvailabilityRequest();

        Mockito.when(service.updateAvailability(
                Mockito.eq(PROFESSIONAL_ID),
                Mockito.eq(AVAILABILITY_ID),
                any()))
                .thenThrow(new AvailabilityNotFoundException());

        mockMvc.perform(put(URI_WITH_ID, PROFESSIONAL_ID, AVAILABILITY_ID)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Remove uma disponibilidade do profissional com sucesso")
    void shouldDeleteAvailabilitySuccessfully() throws Exception {
        Mockito.doNothing()
                .when(service)
                .deleteAvailability(PROFESSIONAL_ID, AVAILABILITY_ID);

        mockMvc.perform(delete(URI_WITH_ID, PROFESSIONAL_ID, AVAILABILITY_ID)
                        .header("Authorization", bearer()))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deleteAvailability(PROFESSIONAL_ID, AVAILABILITY_ID);
    }

    @Test
    @DisplayName("Retorna NotFoundException ao tentar remover uma disponibilidade inexistente")
    void shouldThrowNotFoundExceptionWhenDeleteNonExistingAvailability() throws Exception {
        Mockito.doThrow(new AvailabilityNotFoundException())
                .when(service)
                .deleteAvailability(
                        PROFESSIONAL_ID,
                        AVAILABILITY_ID
                );
        mockMvc.perform(delete(URI_WITH_ID, PROFESSIONAL_ID, AVAILABILITY_ID)
                        .header("Authorization", bearer()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Retorna ForbiddenException ao tentar cadastrar disponibilidade sem Token")
    void shouldThrowUnauthorizedWhenCreateWithoutToken() throws Exception {
        var request = AvailabilityMockDto.createAvailabilityRequestMorning();

        mockMvc.perform(post(URI, PROFESSIONAL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Retorna ForbiddenException ao tentar atualizar disponibilidade sem Token")
    void shouldThrowUnauthorizedWhenUpdateWithoutToken() throws Exception {
        var request = AvailabilityMockDto.updateAvailabilityRequest();
        mockMvc.perform(put(URI_WITH_ID, PROFESSIONAL_ID, AVAILABILITY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Retorna ForbiddenException ao tentar listar disponibilidades sem Token")
    void shouldThrowUnauthorizedWhenListWithoutToken() throws Exception {
        mockMvc.perform(get(URI, PROFESSIONAL_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Retorna ForbiddenException ao tentar remover disponibilidade sem Token")
    void shouldThrowUnauthorizedWhenDeleteWithoutToken() throws Exception {
        mockMvc.perform(delete(URI_WITH_ID, PROFESSIONAL_ID, AVAILABILITY_ID))
                .andExpect(status().isForbidden());
    }
}
