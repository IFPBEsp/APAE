package br.org.apae.api.controllers.vaccine;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("vaccine")
})
@WebMvcTest(VaccineControllerImpl.class)
public class VaccineRemovalTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VaccineApplicationService vaccineService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Deve excluir vacina com sucesso (Retorna 204)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveExcluirVacina() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(vaccineService).deleteVaccine(id);

        mockMvc.perform(delete("/vaccines/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar excluir ID inexistente")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveFalharExclusaoIdInexistente() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new EntityNotFoundException("Vacina não encontrada"))
                .when(vaccineService).deleteVaccine(id);

        mockMvc.perform(delete("/vaccines/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}