package br.org.apae.api.controllers.servicetype;

import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityFilter;
import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.request.UpdateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.professional.domain.exceptions.ServiceTypeConflictException;
import br.org.apae.api.professional.domain.exceptions.ServiceTypeNotFoundException;
import br.org.apae.api.servicetype.application.interfaces.ServiceTypeApplicationService;
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

@WebMvcTest(ServiceTypeControllerImpl.class)
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
class ServiceTypeControllerTest {

    private static final String URI = "/service-types";
    private static final String LEGACY_URI = "/service-areas";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceTypeApplicationService service;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Nested
    @DisplayName("POST /service-types")
    class CreateServiceType {

        @Test
        @DisplayName("Deve criar área de atendimento com sucesso")
        void shouldCreateServiceTypeSuccessfully() throws Exception {
            CreateServiceTypeDTO request = new CreateServiceTypeDTO("Fisioterapia");
            ServiceTypeResponseDTO response = new ServiceTypeResponseDTO(1, "Fisioterapia");

            when(service.createServiceType(any(CreateServiceTypeDTO.class)))
                    .thenReturn(response);

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.area").value("Fisioterapia"));

            ArgumentCaptor<CreateServiceTypeDTO> captor =
                    ArgumentCaptor.forClass(CreateServiceTypeDTO.class);

            verify(service).createServiceType(captor.capture());

            assertThat(captor.getValue().area()).isEqualTo("Fisioterapia");
        }

        @Test
        @DisplayName("Deve retornar BadRequest quando área estiver em branco")
        void shouldReturnBadRequestWhenCreatingServiceTypeWithBlankArea() throws Exception {
            CreateServiceTypeDTO request = new CreateServiceTypeDTO("");

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(service);
        }

        @Test
        @DisplayName("Deve retornar Conflict quando área de atendimento já existir")
        void shouldReturnConflictWhenCreatingDuplicatedServiceType() throws Exception {
            CreateServiceTypeDTO request = new CreateServiceTypeDTO("Fisioterapia");

            when(service.createServiceType(any(CreateServiceTypeDTO.class)))
                    .thenThrow(new ServiceTypeConflictException());

            mockMvc.perform(post(URI)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());

            verify(service).createServiceType(any(CreateServiceTypeDTO.class));
        }
    }

    @Nested
    @DisplayName("GET /service-types")
    class GetAllServiceTypes {

        @Test
        @DisplayName("Deve listar áreas de atendimento com sucesso")
        void shouldGetAllServiceTypesSuccessfully() throws Exception {
            List<ServiceTypeResponseDTO> response = List.of(
                    new ServiceTypeResponseDTO(1, "Fisioterapia"),
                    new ServiceTypeResponseDTO(2, "Psicologia")
            );

            when(service.findAllServiceTypes()).thenReturn(response);

            mockMvc.perform(get(URI))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].area").value("Fisioterapia"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].area").value("Psicologia"));

            verify(service).findAllServiceTypes();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver áreas cadastradas")
        void shouldReturnEmptyListWhenThereAreNoServiceTypes() throws Exception {
            when(service.findAllServiceTypes()).thenReturn(Collections.emptyList());

            mockMvc.perform(get(URI))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(service).findAllServiceTypes();
        }

        @Test
        @DisplayName("Deve listar tipos de atendimento pela rota legada /service-areas")
        void shouldGetAllServiceTypesThroughLegacyRoute() throws Exception {
            List<ServiceTypeResponseDTO> response = List.of(
                    new ServiceTypeResponseDTO(1, "Fisioterapia")
            );

            when(service.findAllServiceTypes()).thenReturn(response);

            mockMvc.perform(get(LEGACY_URI))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].area").value("Fisioterapia"));

            verify(service).findAllServiceTypes();
        }
    }

    @Nested
    @DisplayName("GET /service-types/{id}")
    class FindServiceTypeById {

        @Test
        @DisplayName("Deve buscar área de atendimento por ID com sucesso")
        void shouldFindServiceTypeByIdSuccessfully() throws Exception {
            ServiceTypeResponseDTO response = new ServiceTypeResponseDTO(1, "Fisioterapia");

            when(service.findServiceTypeById(1)).thenReturn(response);

            mockMvc.perform(get(URI + "/{id}", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.area").value("Fisioterapia"));

            verify(service).findServiceTypeById(1);
        }

        @Test
        @DisplayName("Deve retornar NotFound quando área de atendimento não existir")
        void shouldReturnNotFoundWhenServiceTypeDoesNotExist() throws Exception {
            when(service.findServiceTypeById(99))
                    .thenThrow(new ServiceTypeNotFoundException());

            mockMvc.perform(get(URI + "/{id}", 99))
                    .andExpect(status().isNotFound());

            verify(service).findServiceTypeById(99);
        }
    }

    @Nested
    @DisplayName("PUT /service-types/{id}")
    class UpdateServiceType {

        @Test
        @DisplayName("Deve atualizar área de atendimento com sucesso")
        void shouldUpdateServiceTypeSuccessfully() throws Exception {
            UpdateServiceTypeDTO request = new UpdateServiceTypeDTO("Terapia Ocupacional");
            ServiceTypeResponseDTO response = new ServiceTypeResponseDTO(1, "Terapia Ocupacional");

            when(service.updateServiceType(eq(1), any(UpdateServiceTypeDTO.class)))
                    .thenReturn(response);

            mockMvc.perform(put(URI + "/{id}", 1)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.area").value("Terapia Ocupacional"));

            ArgumentCaptor<UpdateServiceTypeDTO> captor =
                    ArgumentCaptor.forClass(UpdateServiceTypeDTO.class);

            verify(service).updateServiceType(eq(1), captor.capture());

            assertThat(captor.getValue().area()).isEqualTo("Terapia Ocupacional");
        }

        @Test
        @DisplayName("Deve retornar BadRequest quando área estiver em branco na atualização")
        void shouldReturnBadRequestWhenUpdatingServiceTypeWithBlankArea() throws Exception {
            UpdateServiceTypeDTO request = new UpdateServiceTypeDTO("");

            mockMvc.perform(put(URI + "/{id}", 1)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(service);
        }

        @Test
        @DisplayName("Deve retornar NotFound quando área de atendimento não existir na atualização")
        void shouldReturnNotFoundWhenUpdatingNonExistentServiceType() throws Exception {
            UpdateServiceTypeDTO request = new UpdateServiceTypeDTO("Fonoaudiologia");

            when(service.updateServiceType(eq(99), any(UpdateServiceTypeDTO.class)))
                    .thenThrow(new ServiceTypeNotFoundException());

            mockMvc.perform(put(URI + "/{id}", 99)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            verify(service).updateServiceType(eq(99), any(UpdateServiceTypeDTO.class));
        }

        @Test
        @DisplayName("Deve retornar Conflict quando área atualizada já existir")
        void shouldReturnConflictWhenUpdatingToDuplicatedServiceType() throws Exception {
            UpdateServiceTypeDTO request = new UpdateServiceTypeDTO("Psicologia");

            when(service.updateServiceType(eq(1), any(UpdateServiceTypeDTO.class)))
                    .thenThrow(new ServiceTypeConflictException());

            mockMvc.perform(put(URI + "/{id}", 1)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());

            verify(service).updateServiceType(eq(1), any(UpdateServiceTypeDTO.class));
        }
    }

    @Nested
    @DisplayName("DELETE /service-types/{id}")
    class DeleteServiceType {

        @Test
        @DisplayName("Deve excluir área de atendimento com sucesso")
        void shouldDeleteServiceTypeSuccessfully() throws Exception {
            doNothing().when(service).deleteServiceType(1);

            mockMvc.perform(delete(URI + "/{id}", 1))
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            verify(service).deleteServiceType(1);
        }

        @Test
        @DisplayName("Deve retornar NotFound quando área de atendimento não existir na exclusão")
        void shouldReturnNotFoundWhenDeletingNonExistentServiceType() throws Exception {
            doThrow(new ServiceTypeNotFoundException()).when(service).deleteServiceType(99);

            mockMvc.perform(delete(URI + "/{id}", 99))
                    .andExpect(status().isNotFound());

            verify(service).deleteServiceType(99);
        }
    }
}
