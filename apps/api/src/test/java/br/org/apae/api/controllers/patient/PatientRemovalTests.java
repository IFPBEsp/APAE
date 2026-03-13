package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.controllers.patient.PatientControllerImpl;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tags({
        @Tag("controller"),
        @Tag("patient")
})
@WebMvcTest(PatientControllerImpl.class)
public class PatientRemovalTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientApplicationService patientService;

    @MockitoBean
    private DisorderApplicationService disorderApplicationService;

    @MockitoBean
    private AnnualRegistryApplicationService annualRegistryApplicationService;

    @MockitoBean
    private ServiceAreaApplicationService serviceAreaApplicationService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should perform logical deletion (deactivate) successfully (Returns 204)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldPerformLogicalDeletionSuccessfully() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(patientService).deletePatient(id);


        mockMvc.perform(patch("/patients/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 Not Found when trying to deactivate non-existent ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new PatientNotFoundException()).when(patientService).deletePatient(id);

        mockMvc.perform(patch("/patients/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
