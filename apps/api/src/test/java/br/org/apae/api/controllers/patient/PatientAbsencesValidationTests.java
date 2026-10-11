package br.org.apae.api.controllers.patient;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.helpers.AuthTestHelper;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientControllerImpl.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SpringDataWebConfiguration.class, SecurityConfiguration.class, GlobalExceptionHandler.class})
@Tag("patient")
@Tag("unit")
@Tag("controller")
public class PatientAbsencesValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientApplicationService patientService;

    @MockitoBean
    private DisorderApplicationService disorderService;

    @MockitoBean
    private AnnualRegistryApplicationService annualRegistryService;

    @MockitoBean
    private ServiceAreaApplicationService serviceAreaService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setupAuth() {
        AuthTestHelper.mockAuthenticatedUser(jwtProvider, userService);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void shouldReturnBadRequestWhenMinAbsencesIsInvalid(int minAbsences) throws Exception {
        mockMvc.perform(get("/patients/with-absences")
                        .header("Authorization", AuthTestHelper.bearerToken())
                        .param("minAbsences", String.valueOf(minAbsences)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(patientService);
    }
}
