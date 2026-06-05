package br.org.apae.api.controllers.dashboard;

import br.org.apae.api.common.dto.dashboard.response.DashboardOverviewResponseDTO;
import br.org.apae.api.dashboard.application.interfaces.DashboardApplicationService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.application.internal.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardControllerImpl.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardApplicationService dashboardService;

    // Beans de infraestrutura de segurança necessários para carregar o contexto do Spring
    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    private final DashboardOverviewResponseDTO mockResponse =
            new DashboardOverviewResponseDTO(100L, 15L);

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Deve retornar 200 e usar minAbsences padrão (3) quando não informado")
    void getOverview_ShouldReturn200WithDefaultMinAbsences() throws Exception {
        Mockito.when(dashboardService.getOverview(3)).thenReturn(mockResponse);

        mockMvc.perform(get("/dashboard/overview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatients").value(100))
                .andExpect(jsonPath("$.totalPatientsWithAbsences").value(15));

        Mockito.verify(dashboardService).getOverview(3);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Deve retornar 200 e usar minAbsences customizado via query param")
    void getOverview_ShouldReturn200WithCustomMinAbsences() throws Exception {
        Mockito.when(dashboardService.getOverview(5)).thenReturn(mockResponse);

        mockMvc.perform(get("/dashboard/overview")
                        .param("minAbsences", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatients").value(100))
                .andExpect(jsonPath("$.totalPatientsWithAbsences").value(15));

        Mockito.verify(dashboardService).getOverview(5);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Deve retornar 200 e aceitar minAbsences igual a 0 (limite inferior)")
    void getOverview_ShouldReturn200WithZeroMinAbsences() throws Exception {
        Mockito.when(dashboardService.getOverview(0)).thenReturn(mockResponse);

        mockMvc.perform(get("/dashboard/overview")
                        .param("minAbsences", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatients").value(100))
                .andExpect(jsonPath("$.totalPatientsWithAbsences").value(15));

        Mockito.verify(dashboardService).getOverview(0);
    }

    @Test
    @DisplayName("Deve retornar 401 quando o usuário não estiver autenticado")
    void getOverview_ShouldReturn401WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/dashboard/overview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        Mockito.verifyNoInteractions(dashboardService);
    }
}