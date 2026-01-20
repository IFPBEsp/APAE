package br.org.apae.api.controllers.vaccine;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.controllers.patient.mocks.vaccine.VaccineCreator;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
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

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("vaccine")
})
@WebMvcTest(VaccineControllerImpl.class)
public class VaccineRetrievalTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VaccineApplicationService vaccineService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Deve buscar vacina por ID com sucesso (Retorna 200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBuscarVacinaPorId() throws Exception {
        VaccineResponseDTO responseDTO = VaccineCreator.createResponse();
        UUID id = responseDTO.id();

        when(vaccineService.findVaccineById(id)).thenReturn(responseDTO);

        mockMvc.perform(get("/vaccines/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(responseDTO.name()));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar ID inexistente")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarErroComIdInexistente() throws Exception {
        UUID id = UUID.randomUUID();

        when(vaccineService.findVaccineById(id))
                .thenThrow(new EntityNotFoundException("Vacina não encontrada"));

        mockMvc.perform(get("/vaccines/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar todas as vacinas (Retorna 200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarTodasAsVacinas() throws Exception {
        VaccineResponseDTO responseDTO = VaccineCreator.createResponse();
        when(vaccineService.findAllVaccines()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/vaccines")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("BCG"));
    }

    @Test
    @DisplayName("Deve buscar vacina por nome (Retorna 200)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveBuscarVacinaPorNome() throws Exception {
        VaccineResponseDTO responseDTO = VaccineCreator.createResponse();
        String name = "BCG";

        when(vaccineService.findVaccineByName(name)).thenReturn(responseDTO);

        mockMvc.perform(get("/vaccines/search/by-name")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BCG"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar por nome inexistente")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornarErroComNomeInexistente() throws Exception {
        String name = "Inexistente";
        when(vaccineService.findVaccineByName(name))
                .thenThrow(new EntityNotFoundException("Vacina não encontrada"));

        mockMvc.perform(get("/vaccines/search/by-name")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}