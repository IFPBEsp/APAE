package br.org.apae.api.controllers.vaccine;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import br.org.apae.api.patient.domain.exceptions.VaccineConflictException;
import br.org.apae.api.patient.domain.exceptions.VaccineNotFoundException;
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

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VaccineControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class,
        GlobalExceptionHandler.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
public class VaccineControllerTest {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {}

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VaccineApplicationService vaccineService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private static final String BASE_URL = "/vaccines";

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    @Test
    @DisplayName("Deve criar vacina com sucesso (201) quando dados são válidos")
    void shouldCreateVaccineSuccess() throws Exception {
        CreateVaccineDTO requestDto = new CreateVaccineDTO("BCG");
        UUID idGerado = UUID.randomUUID();
        VaccineResponseDTO responseDto = new VaccineResponseDTO(idGerado, requestDto.name(), false);

        when(vaccineService.createVaccine(any(CreateVaccineDTO.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(idGerado.toString()))
                .andExpect(jsonPath("$.name").value(requestDto.name()));
    }

    @Test
    @DisplayName("Deve retornar Erro de Validação (400) quando nome da vacina é inválido")
    void shouldReturnBadRequestWhenNameIsInvalid() throws Exception {
        CreateVaccineDTO invalidDto = new CreateVaccineDTO("");

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(vaccineService, never()).createVaccine(any());
    }

    @Test
    @DisplayName("Deve retornar Conflict (409) ao tentar criar vacina com nome duplicado")
    void shouldReturnConflictWhenCreateVaccineWithDuplicateName() throws Exception {
        CreateVaccineDTO requestDto = new CreateVaccineDTO("Hepatite B");
        when(vaccineService.createVaccine(any(CreateVaccineDTO.class)))
                .thenThrow(new VaccineConflictException(requestDto.name()));

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve listar todas as vacinas com sucesso (200)")
    void shouldGetAllVaccinesSuccess() throws Exception {
        VaccineResponseDTO v1 = new VaccineResponseDTO(UUID.randomUUID(), "BCG", false);
        VaccineResponseDTO v2 = new VaccineResponseDTO(UUID.randomUUID(), "Viral", true);
        List<VaccineResponseDTO> lista = List.of(v1, v2);

        when(vaccineService.findAllVaccines()).thenReturn(lista);

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("BCG")))
                .andExpect(jsonPath("$[1].name", is("Viral")));
    }

    @Test
    @DisplayName("Deve buscar vacina por ID com sucesso (200)")
    void shouldSearchVaccineByIdSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        VaccineResponseDTO responseDto = new VaccineResponseDTO(id, "BCG", false);

        when(vaccineService.findVaccineById(id)).thenReturn(responseDto);

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("BCG"));
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao buscar vacina por ID inexistente")
    void shouldReturnNotFoundWhenSearchVaccineByIdNonExistent() throws Exception {
        UUID id = UUID.randomUUID();
        when(vaccineService.findVaccineById(id))
                .thenThrow(new VaccineNotFoundException());

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve buscar vacina por nome com sucesso (200)")
    void shouldSearchVaccineByNameSuccess() throws Exception {
        String name = "BCG";
        VaccineResponseDTO responseDto = new VaccineResponseDTO(UUID.randomUUID(), name, false);

        when(vaccineService.findVaccineByName(name)).thenReturn(responseDto);

        mockMvc.perform(get(BASE_URL + "/search/by-name")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao buscar vacina por nome inexistente")
    void shouldReturnNotFoundWhenSearchVaccineByNameNonExistent() throws Exception {
        String name = "Inexistente";
        when(vaccineService.findVaccineByName(name))
                .thenThrow(new VaccineNotFoundException(name));

        mockMvc.perform(get(BASE_URL + "/search/by-name")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("name", name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar vacina com sucesso (200)")
    void shouldUpdateVaccineSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        CreateVaccineDTO updateDto = new CreateVaccineDTO("BCG Atualizada");
        VaccineResponseDTO responseDto = new VaccineResponseDTO(id, updateDto.name(), false);

        when(vaccineService.updateVaccine(eq(id), any(CreateVaccineDTO.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(updateDto.name()));
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao tentar atualizar vacina inexistente")
    void shouldReturnNotFoundWhenUpdateVaccineNonExistent() throws Exception {
        UUID id = UUID.randomUUID();
        CreateVaccineDTO updateDto = new CreateVaccineDTO("BCG");
        when(vaccineService.updateVaccine(eq(id), any(CreateVaccineDTO.class)))
                .thenThrow(new VaccineNotFoundException());

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar vacina com sucesso (204)")
    void shouldDeleteVaccineSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(vaccineService).deleteVaccine(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        verify(vaccineService).deleteVaccine(id);
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao deletar vacina inexistente")
    void shouldReturnNotFoundWhenDeleteVaccineNonExistent() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new VaccineNotFoundException())
                .when(vaccineService).deleteVaccine(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());
    }
}