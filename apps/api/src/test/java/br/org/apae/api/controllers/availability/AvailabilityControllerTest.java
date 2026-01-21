package br.org.apae.api.controllers.availability;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.application.interfaces.AvailabilityApplicationService;
import br.org.apae.api.professional.domain.exceptions.AvailabilityConflictException;
import br.org.apae.api.professional.domain.exceptions.AvailabilityNotFoundException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Tag("controller")
@Tag("availability")
@WebMvcTest(controllers = AvailabilityControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class
})
@DisplayName("AvailabilityController - Testes de Endpoints REST")
class AvailabilityControllerTest {

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

    private UUID professionalId;
    private UUID availabilityId;
    private CreateAvailabilityDTO createDto;
    private UpdateAvailabilityDTO updateDto;
    private AvailabilityResponseDTO responseDto;

    @BeforeEach
    void setup() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);

        professionalId = UUID.randomUUID();
        availabilityId = UUID.randomUUID();
        createDto = new CreateAvailabilityDTO("SEGUNDA", "MANHA");
        updateDto = new UpdateAvailabilityDTO("TERCA", "TARDE");
        responseDto = new AvailabilityResponseDTO(availabilityId, "SEGUNDA", "MANHA");
    }

    @Test
    @DisplayName("POST /availabilities/professional/{id} - Deve criar nova disponibilidade com sucesso")
    void shouldCreateAvailabilitySuccessfully() throws Exception {
        Mockito.when(service.createAvailability(eq(professionalId), any(CreateAvailabilityDTO.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/availabilities/professional/{professionalId}", professionalId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(availabilityId.toString()))
                .andExpect(jsonPath("$.day").value("SEGUNDA"))
                .andExpect(jsonPath("$.shift").value("MANHA"));

        Mockito.verify(service).createAvailability(eq(professionalId), any(CreateAvailabilityDTO.class));
    }

    @Test
    @DisplayName("POST /availabilities/professional/{id} - Deve retornar erro ao criar com campos obrigatórios vazios")
    void shouldReturnErrorWhenCreatingWithEmptyFields() throws Exception {
        CreateAvailabilityDTO invalidDto = new CreateAvailabilityDTO("", "");

        mockMvc.perform(post("/availabilities/professional/{professionalId}", professionalId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(service, Mockito.never()).createAvailability(any(), any());
    }

    @Test
    @DisplayName("POST /availabilities/professional/{id} - Deve retornar erro quando profissional não existe")
    void shouldReturnErrorWhenProfessionalNotFound() throws Exception {
        Mockito.when(service.createAvailability(eq(professionalId), any(CreateAvailabilityDTO.class)))
                .thenThrow(new HealthProfessionalNotFoundException());

        mockMvc.perform(post("/availabilities/professional/{professionalId}", professionalId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isNotFound());

        Mockito.verify(service).createAvailability(eq(professionalId), any(CreateAvailabilityDTO.class));
    }

    @Test
    @DisplayName("POST /availabilities/professional/{id} - Deve validar conflitos de horários")
    void shouldValidateTimeConflict() throws Exception {
        Mockito.when(service.createAvailability(eq(professionalId), any(CreateAvailabilityDTO.class)))
                .thenThrow(new AvailabilityConflictException());

        mockMvc.perform(post("/availabilities/professional/{professionalId}", professionalId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isConflict());

        Mockito.verify(service).createAvailability(eq(professionalId), any(CreateAvailabilityDTO.class));
    }

    @Test
    @DisplayName("GET /availabilities - Deve listar todas as disponibilidades registradas")
    void shouldListAllAvailabilities() throws Exception {
        AvailabilityResponseDTO availability1 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "SEGUNDA", "MANHA");
        AvailabilityResponseDTO availability2 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "TERCA", "TARDE");
        List<AvailabilityResponseDTO> availabilities = Arrays.asList(availability1, availability2);

        Mockito.when(service.findAllAvailabilities()).thenReturn(availabilities);

        mockMvc.perform(get("/availabilities")
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].day").value("SEGUNDA"))
                .andExpect(jsonPath("$[0].shift").value("MANHA"))
                .andExpect(jsonPath("$[1].day").value("TERCA"))
                .andExpect(jsonPath("$[1].shift").value("TARDE"));

        Mockito.verify(service).findAllAvailabilities();
    }

    @Test
    @DisplayName("GET /availabilities/{id} - Deve retornar uma disponibilidade existente")
    void shouldReturnExistingAvailability() throws Exception {
        Mockito.when(service.findAvailabilityById(availabilityId)).thenReturn(responseDto);

        mockMvc.perform(get("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availabilityId.toString()))
                .andExpect(jsonPath("$.day").value("SEGUNDA"))
                .andExpect(jsonPath("$.shift").value("MANHA"));

        Mockito.verify(service).findAvailabilityById(availabilityId);
    }

    @Test
    @DisplayName("GET /availabilities/{id} - Deve retornar erro quando ID não existe")
    void shouldReturnErrorWhenIdDoesNotExist() throws Exception {
        Mockito.when(service.findAvailabilityById(availabilityId))
                .thenThrow(new AvailabilityNotFoundException());

        mockMvc.perform(get("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());

        Mockito.verify(service).findAvailabilityById(availabilityId);
    }

    @Test
    @DisplayName("GET /availabilities/professional/{id} - Deve listar disponibilidades por profissional")
    void shouldListAvailabilitiesByProfessional() throws Exception {
        AvailabilityResponseDTO availability1 = new AvailabilityResponseDTO(
                UUID.randomUUID(), "SEGUNDA", "MANHA");
        List<AvailabilityResponseDTO> availabilities = Arrays.asList(availability1);

        Mockito.when(service.findAvailabilitiesByProfessional(professionalId))
                .thenReturn(availabilities);

        mockMvc.perform(get("/availabilities/professional/{professionalId}", professionalId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].day").value("SEGUNDA"))
                .andExpect(jsonPath("$[0].shift").value("MANHA"));

        Mockito.verify(service).findAvailabilitiesByProfessional(professionalId);
    }

    @Test
    @DisplayName("PUT /availabilities/{id} - Deve atualizar horários ou status")
    void shouldUpdateAvailability() throws Exception {
        AvailabilityResponseDTO updatedDto = new AvailabilityResponseDTO(
                availabilityId, "TERCA", "TARDE");

        Mockito.when(service.updateAvailability(eq(availabilityId), any(UpdateAvailabilityDTO.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(availabilityId.toString()))
                .andExpect(jsonPath("$.day").value("TERCA"))
                .andExpect(jsonPath("$.shift").value("TARDE"));

        Mockito.verify(service).updateAvailability(eq(availabilityId), any(UpdateAvailabilityDTO.class));
    }

    @Test
    @DisplayName("PUT /availabilities/{id} - Deve retornar erro ao atualizar disponibilidade inexistente")
    void shouldReturnErrorWhenUpdatingNonExistentAvailability() throws Exception {
        Mockito.when(service.updateAvailability(eq(availabilityId), any(UpdateAvailabilityDTO.class)))
                .thenThrow(new AvailabilityNotFoundException());

        mockMvc.perform(put("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());

        Mockito.verify(service).updateAvailability(eq(availabilityId), any(UpdateAvailabilityDTO.class));
    }

    @Test
    @DisplayName("PUT /availabilities/{id} - Deve validar conflito ao atualizar")
    void shouldValidateConflictWhenUpdating() throws Exception {
        Mockito.when(service.updateAvailability(eq(availabilityId), any(UpdateAvailabilityDTO.class)))
                .thenThrow(new AvailabilityConflictException());

        mockMvc.perform(put("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isConflict());

        Mockito.verify(service).updateAvailability(eq(availabilityId), any(UpdateAvailabilityDTO.class));
    }

    @Test
    @DisplayName("DELETE /availabilities/{id} - Deve excluir uma disponibilidade existente")
    void shouldDeleteAvailability() throws Exception {
        Mockito.doNothing().when(service).deleteAvailability(availabilityId);

        mockMvc.perform(delete("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deleteAvailability(availabilityId);
    }

    @Test
    @DisplayName("DELETE /availabilities/{id} - Deve retornar erro ao excluir disponibilidade inexistente")
    void shouldReturnErrorWhenDeletingNonExistentAvailability() throws Exception {
        Mockito.doThrow(new AvailabilityNotFoundException())
                .when(service).deleteAvailability(availabilityId);

        mockMvc.perform(delete("/availabilities/{id}", availabilityId)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());

        Mockito.verify(service).deleteAvailability(availabilityId);
    }
}
