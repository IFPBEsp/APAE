package br.org.apae.api.controllers.vaccine;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.controllers.patient.mocks.vaccine.VaccineCreator;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("vaccine")
})
@WebMvcTest(VaccineControllerImpl.class)
public class VaccineModificationTests {

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
    @DisplayName("Deve atualizar vacina com sucesso (Retorna 200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveAtualizarVacinaComSucesso() throws Exception {
        VaccineResponseDTO responseDTO = VaccineCreator.createResponse();
        CreateVaccineDTO updateDTO = VaccineCreator.createRequest();
        UUID id = responseDTO.id();

        when(vaccineService.updateVaccine(eq(id), any(CreateVaccineDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/vaccines/{id}", id)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(responseDTO.name()));
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar ID inexistente")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveFalharUpdateIdInexistente() throws Exception {
        UUID id = UUID.randomUUID();
        CreateVaccineDTO updateDTO = VaccineCreator.createRequest();

        when(vaccineService.updateVaccine(eq(id), any(CreateVaccineDTO.class)))
                .thenThrow(new EntityNotFoundException("Vacina não encontrada"));

        mockMvc.perform(put("/vaccines/{id}", id)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao atualizar com dados inválidos")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveFalharValidacaoUpdate() throws Exception {
        UUID id = UUID.randomUUID();
        CreateVaccineDTO invalidDTO = VaccineCreator.createInvalidRequest();

        mockMvc.perform(put("/vaccines/{id}", id)
                        .content(objectMapper.writeValueAsString(invalidDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}