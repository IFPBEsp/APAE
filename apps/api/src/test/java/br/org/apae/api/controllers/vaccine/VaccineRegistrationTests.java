package br.org.apae.api.controllers.vaccine;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.controllers.patient.mocks.vaccine.VaccineCreator;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("vaccine")
})
@WebMvcTest(VaccineControllerImpl.class)
public class VaccineRegistrationTests {

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

    @Test
    @DisplayName("Deve criar uma nova vacina com sucesso (Retorna 201)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveCriarVacinaComSucesso() throws Exception {
        CreateVaccineDTO requestDTO = VaccineCreator.createRequest();
        VaccineResponseDTO responseDTO = VaccineCreator.createResponse();

        when(vaccineService.createVaccine(any(CreateVaccineDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/vaccines")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.id().toString()))
                .andExpect(jsonPath("$.name").value(responseDTO.name()));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar vacina com nome inválido")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveFalharValidacaoCreate() throws Exception {
        CreateVaccineDTO invalidDTO = VaccineCreator.createInvalidRequest();

        mockMvc.perform(post("/vaccines")
                        .content(objectMapper.writeValueAsString(invalidDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(vaccineService, never()).createVaccine(any());
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict se o nome da vacina já existir")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarConflitoSeNomeDuplicado() throws Exception {
        CreateVaccineDTO requestDTO = VaccineCreator.createRequest();

        when(vaccineService.createVaccine(any(CreateVaccineDTO.class)))
                .thenThrow(new DataIntegrityViolationException("Vacina já existe"));

        mockMvc.perform(post("/vaccines")
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isConflict());
    }
}