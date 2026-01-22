package br.org.apae.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.org.apae.api.controllers.servicearea.ServiceAreaControllerImpl;
import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaConflictException;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

@Tag("controller")
@Tag("service-area")
@WebMvcTest(controllers = ServiceAreaControllerImpl.class)
@AutoConfigureMockMvc
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class
})
class ServiceAreaControllerImplTest {

    private static final String URI = "/service-areas";
    private static final String URI_WITH_ID = URI + "/{id}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceAreaApplicationService service;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private ServiceAreaResponseDTO responseDTO;

    private String bearer() {
        return AuthTestHelper.bearerToken();
    }

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
        responseDTO = new ServiceAreaResponseDTO(1, "SAÚDE");
    }

    @Test
    void shouldCreateServiceAreaSuccessfully() throws Exception {
        CreateServiceAreaDTO request = new CreateServiceAreaDTO("SAÚDE");

        Mockito.when(service.createServiceArea(any(CreateServiceAreaDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post(URI)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.area").value("SAÚDE"));

        Mockito.verify(service)
                .createServiceArea(any(CreateServiceAreaDTO.class));
    }

    @Test
    void shouldReturnAllServiceAreas() throws Exception {
        List<ServiceAreaResponseDTO> responses = List.of(
                new ServiceAreaResponseDTO(1, "SAÚDE"),
                new ServiceAreaResponseDTO(2, "EDUCAÇÃO")
        );

        Mockito.when(service.findAllServiceAreas())
                .thenReturn(responses);

        mockMvc.perform(get(URI)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].area").value("SAÚDE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].area").value("EDUCAÇÃO"));

        Mockito.verify(service).findAllServiceAreas();
    }

    @Test
    void shouldFindServiceAreaById() throws Exception {
        Mockito.when(service.findServiceAreaById(1))
                .thenReturn(responseDTO);

        mockMvc.perform(get(URI_WITH_ID, 1)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.area").value("SAÚDE"));

        Mockito.verify(service).findServiceAreaById(1);
    }

    @Test
    void shouldUpdateServiceAreaSuccessfully() throws Exception {
        UpdateServiceAreaDTO request = new UpdateServiceAreaDTO("EDUCAÇÃO");

        ServiceAreaResponseDTO updatedResponse =
                new ServiceAreaResponseDTO(1, "EDUCAÇÃO");

        Mockito.when(service.updateServiceArea(Mockito.eq(1), any(UpdateServiceAreaDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put(URI_WITH_ID, 1)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.area").value("EDUCAÇÃO"));

        Mockito.verify(service)
                .updateServiceArea(Mockito.eq(1), any(UpdateServiceAreaDTO.class));
    }

    @Test
    void shouldDeleteServiceAreaSuccessfully() throws Exception {
        Mockito.doNothing().when(service).deleteServiceArea(1);

        mockMvc.perform(delete(URI_WITH_ID, 1)
                        .header("Authorization", bearer()))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deleteServiceArea(1);
    }

    @Test
    void shouldReturnNotFoundWhenServiceAreaDoesNotExist() throws Exception {
        Mockito.when(service.findServiceAreaById(99))
                .thenThrow(new ServiceAreaNotFoundException());

        mockMvc.perform(get(URI_WITH_ID, 99)
                        .header("Authorization", bearer()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenUpdateNonExistingServiceArea() throws Exception {
        UpdateServiceAreaDTO request = new UpdateServiceAreaDTO("EDUCAÇÃO");

        Mockito.when(service.updateServiceArea(Mockito.eq(99), any()))
                .thenThrow(new ServiceAreaNotFoundException());

        mockMvc.perform(put(URI_WITH_ID, 99)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnConflictWhenServiceAreaAlreadyExists() throws Exception {
        CreateServiceAreaDTO request = new CreateServiceAreaDTO("SAÚDE");

        Mockito.when(service.createServiceArea(any()))
                .thenThrow(new ServiceAreaConflictException());

        mockMvc.perform(post(URI)
                        .header("Authorization", bearer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnEmptyListWhenNoServiceAreasExist() throws Exception {
        Mockito.when(service.findAllServiceAreas())
                .thenReturn(List.of());

        mockMvc.perform(get(URI)
                        .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
