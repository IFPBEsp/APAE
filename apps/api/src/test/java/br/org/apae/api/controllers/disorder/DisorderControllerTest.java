package br.org.apae.api.controllers.disorder;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.domain.exceptions.DisorderConflictException;
import br.org.apae.api.patient.domain.exceptions.DisorderNotFoundException;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DisorderControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({
        SpringDataWebConfiguration.class,
        SecurityConfiguration.class
})
@Tag("patient")
@Tag("unit")
@Tag("controller")
class DisorderControllerTest {

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {

    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DisorderApplicationService disorderService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private static final String BASE_URL = "/disorders";

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    @Test
    @DisplayName("Deve criar transtorno com sucesso (201)")
    void shouldCreateDisordSucess() throws Exception {
        CreateDisorderDTO requestDto = new CreateDisorderDTO("Transtorno Ansioso");
        UUID idGerado = UUID.randomUUID();

        DisorderResponseDTO responseDto = new DisorderResponseDTO(idGerado, requestDto.name(), false);

        when(disorderService.createDisorder(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idGerado.toString()))
                .andExpect(jsonPath("$.name").value(requestDto.name()));
    }

    @Test
    @DisplayName("Deve buscar todos os transtornos com sucesso (200)")
    void shouldGetAllDisordersSucess() throws Exception {
        DisorderResponseDTO d1 = new DisorderResponseDTO(UUID.randomUUID(), "Transtorno A", false);
        DisorderResponseDTO d2 = new DisorderResponseDTO(UUID.randomUUID(), "Transtorno B", false);
        List<DisorderResponseDTO> lista = Arrays.asList(d1, d2);

        when(disorderService.findAllDisorders()).thenReturn(lista);

        mockMvc.perform(get(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Transtorno A")))
                .andExpect(jsonPath("$[1].name", is("Transtorno B")));
    }

    @Test
    @DisplayName("Deve buscar transtorno por ID com sucesso (200)")
    void shouldSearchDisordsByIdSucess() throws Exception {
        UUID id = UUID.randomUUID();
        DisorderResponseDTO responseDto = new DisorderResponseDTO(id, "Transtorno Específico", false);

        when(disorderService.findDisorderById(id)).thenReturn(responseDto);

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Transtorno Específico"));
    }

    @Test
    @DisplayName("Deve atualizar transtorno com sucesso (200)")
    void SouldUpdateDisorderSucess() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateDisorderDTO updateDto = new UpdateDisorderDTO("Transtorno Atualizado");
        DisorderResponseDTO responseDto = new DisorderResponseDTO(id, updateDto.name(), false);

        when(disorderService.updateDisorder(id, updateDto)).thenReturn(responseDto);

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
    @DisplayName("Deve deletar transtorno com sucesso (204)")
    void shouldRemoveDisorderSucess() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/{id}", id).header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNoContent());

        verify(disorderService).deleteDisorder(id);
    }

    @Test
    @DisplayName("Deve retornar Conflict (409) ao tentar criar transtorno com nome duplicado")
    void shouldReturnConflictWhenCreateDisorderWithDuplicateName() throws Exception {
        CreateDisorderDTO requestDto = new CreateDisorderDTO("Transtorno Duplicado");

        when(disorderService.createDisorder(requestDto))
                .thenThrow(new DisorderConflictException(requestDto.name()));

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao buscar transtorno inexistente")
    void shouldReturnNotFoundWhenSearchDisorderByIdNonExistent() throws Exception {
        UUID id = UUID.randomUUID();

        when(disorderService.findDisorderById(id))
                .thenThrow(new DisorderNotFoundException());

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao tentar atualizar transtorno inexistente")
    void shouldReturnNotFoundWhenUpdateDisorderNonExistent() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateDisorderDTO updateDto = new UpdateDisorderDTO("Nome qualquer");

        when(disorderService.updateDisorder(id, updateDto))
                .thenThrow(new DisorderNotFoundException());

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Conflict (409) ao tentar atualizar transtorno gerando nome duplicado")
    void shouldReturnConflictWhenUpdateDisorderWithDuplicateName() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateDisorderDTO updateDto = new UpdateDisorderDTO("Nome Já Existe");

        when(disorderService.updateDisorder(id, updateDto))
                .thenThrow(new DisorderConflictException(updateDto.name()));

        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar NotFound (404) ao tentar deletar transtorno inexistente")
    void shouldReturnNotFoundWhenDeleteDisorderNonExistent() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new DisorderNotFoundException()).when(disorderService).deleteDisorder(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id).header("Authorization", AuthTestHelper.bearerToken()))
                .andExpect(status().isNotFound());
    }
}