package br.org.apae.api.controllers.servicearea;

import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityFilter;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaConflictException;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceAreaControllerImpl.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class ServiceAreaControllerTest {

    private static final String URI = "/service-areas";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceAreaApplicationService service;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Nested
    @DisplayName("POST /service-areas")
    class CreateServiceArea {

        @Test
        @DisplayName("Deve criar área de atendimento com sucesso")
        void shouldCreateServiceAreaSuccessfully() throws Exception {
            CreateServiceAreaDTO request = new CreateServiceAreaDTO("Fisioterapia");
            ServiceAreaResponseDTO response = new ServiceAreaResponseDTO(1, "Fisioterapia");

            when(service.createServiceArea(any(CreateServiceAreaDTO.class)))
                    .thenReturn(response);

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.area").value("Fisioterapia"));

            ArgumentCaptor<CreateServiceAreaDTO> captor =
                    ArgumentCaptor.forClass(CreateServiceAreaDTO.class);

            verify(service).createServiceArea(captor.capture());

            assertThat(captor.getValue().area()).isEqualTo("Fisioterapia");
        }

        @Test
        @DisplayName("Deve retornar BadRequest quando área estiver em branco")
        void shouldReturnBadRequestWhenCreatingServiceAreaWithBlankArea() throws Exception {
            CreateServiceAreaDTO request = new CreateServiceAreaDTO("");

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(service);
        }

        @Test
        @DisplayName("Deve retornar Conflict quando área de atendimento já existir")
        void shouldReturnConflictWhenCreatingDuplicatedServiceArea() throws Exception {
            CreateServiceAreaDTO request = new CreateServiceAreaDTO("Fisioterapia");

            when(service.createServiceArea(any(CreateServiceAreaDTO.class)))
                    .thenThrow(new ServiceAreaConflictException());

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());

            verify(service).createServiceArea(any(CreateServiceAreaDTO.class));
        }
    }

    @Nested
    @DisplayName("GET /service-areas")
    class GetAllServiceAreas {

        @Test
        @DisplayName("Deve listar áreas de atendimento com sucesso")
        void shouldGetAllServiceAreasSuccessfully() throws Exception {
            List<ServiceAreaResponseDTO> response = List.of(
                    new ServiceAreaResponseDTO(1, "Fisioterapia"),
                    new ServiceAreaResponseDTO(2, "Psicologia")
            );

            when(service.findAllServiceAreas()).thenReturn(response);

            mockMvc.perform(get(URI))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].area").value("Fisioterapia"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].area").value("Psicologia"));

            verify(service).findAllServiceAreas();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver áreas cadastradas")
        void shouldReturnEmptyListWhenThereAreNoServiceAreas() throws Exception {
            when(service.findAllServiceAreas()).thenReturn(Collections.emptyList());

            mockMvc.perform(get(URI))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(service).findAllServiceAreas();
        }
    }

    @Nested
    @DisplayName("GET /service-areas/{id}")
    class FindServiceAreaById {

        @Test
        @DisplayName("Deve buscar área de atendimento por ID com sucesso")
        void shouldFindServiceAreaByIdSuccessfully() throws Exception {
            ServiceAreaResponseDTO response = new ServiceAreaResponseDTO(1, "Fisioterapia");

            when(service.findServiceAreaById(1)).thenReturn(response);

            mockMvc.perform(get(URI + "/{id}", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.area").value("Fisioterapia"));

            verify(service).findServiceAreaById(1);
        }

        @Test
        @DisplayName("Deve retornar NotFound quando área de atendimento não existir")
        void shouldReturnNotFoundWhenServiceAreaDoesNotExist() throws Exception {
            when(service.findServiceAreaById(99))
                    .thenThrow(new ServiceAreaNotFoundException());

            mockMvc.perform(get(URI + "/{id}", 99))
                    .andExpect(status().isNotFound());

            verify(service).findServiceAreaById(99);
        }
    }

    @Nested
    @DisplayName("PUT /service-areas/{id}")
    class UpdateServiceArea {

        @Test
        @DisplayName("Deve atualizar área de atendimento com sucesso")
        void shouldUpdateServiceAreaSuccessfully() throws Exception {
            UpdateServiceAreaDTO request = new UpdateServiceAreaDTO("Terapia Ocupacional");
            ServiceAreaResponseDTO response = new ServiceAreaResponseDTO(1, "Terapia Ocupacional");

            when(service.updateServiceArea(eq(1), any(UpdateServiceAreaDTO.class)))
                    .thenReturn(response);

            mockMvc.perform(put(URI + "/{id}", 1)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.area").value("Terapia Ocupacional"));

            ArgumentCaptor<UpdateServiceAreaDTO> captor =
                    ArgumentCaptor.forClass(UpdateServiceAreaDTO.class);

            verify(service).updateServiceArea(eq(1), captor.capture());

            assertThat(captor.getValue().area()).isEqualTo("Terapia Ocupacional");
        }

        @Test
        @DisplayName("Deve retornar BadRequest quando área estiver em branco na atualização")
        void shouldReturnBadRequestWhenUpdatingServiceAreaWithBlankArea() throws Exception {
            UpdateServiceAreaDTO request = new UpdateServiceAreaDTO("");

            mockMvc.perform(put(URI + "/{id}", 1)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(service);
        }

        @Test
        @DisplayName("Deve retornar NotFound quando área de atendimento não existir na atualização")
        void shouldReturnNotFoundWhenUpdatingNonExistentServiceArea() throws Exception {
            UpdateServiceAreaDTO request = new UpdateServiceAreaDTO("Fonoaudiologia");

            when(service.updateServiceArea(eq(99), any(UpdateServiceAreaDTO.class)))
                    .thenThrow(new ServiceAreaNotFoundException());

            mockMvc.perform(put(URI + "/{id}", 99)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            verify(service).updateServiceArea(eq(99), any(UpdateServiceAreaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar Conflict quando área atualizada já existir")
        void shouldReturnConflictWhenUpdatingToDuplicatedServiceArea() throws Exception {
            UpdateServiceAreaDTO request = new UpdateServiceAreaDTO("Psicologia");

            when(service.updateServiceArea(eq(1), any(UpdateServiceAreaDTO.class)))
                    .thenThrow(new ServiceAreaConflictException());

            mockMvc.perform(put(URI + "/{id}", 1)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());

            verify(service).updateServiceArea(eq(1), any(UpdateServiceAreaDTO.class));
        }
    }

    @Nested
    @DisplayName("DELETE /service-areas/{id}")
    class DeleteServiceArea {

        @Test
        @DisplayName("Deve excluir área de atendimento com sucesso")
        void shouldDeleteServiceAreaSuccessfully() throws Exception {
            doNothing().when(service).deleteServiceArea(1);

            mockMvc.perform(delete(URI + "/{id}", 1))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(service).deleteServiceArea(1);
        }

        @Test
        @DisplayName("Deve retornar NotFound quando área de atendimento não existir na exclusão")
        void shouldReturnNotFoundWhenDeletingNonExistentServiceArea() throws Exception {
            doThrow(new ServiceAreaNotFoundException()).when(service).deleteServiceArea(99);

            mockMvc.perform(delete(URI + "/{id}", 99))
                    .andExpect(status().isNotFound());

            verify(service).deleteServiceArea(99);
        }
    }
}
