package br.org.apae.api.controllers.auth;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.org.apae.api.auth.application.interfaces.AuthApplicationService;
import br.org.apae.api.auth.application.internal.AuthApplicationServiceImpl;
import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.domain.interfaces.TokenProvider;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.domain.repository.PasswordRecoveryTokenRepository;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.RestAccessDeniedHandler;
import br.org.apae.api.auth.infrastructure.security.RestAuthenticationEntryPoint;
import br.org.apae.api.auth.infrastructure.security.SecurityConfiguration;
import br.org.apae.api.auth.infrastructure.security.SecurityFilter;
import br.org.apae.api.common.dto.auth.request.SignInDTO;
import br.org.apae.api.common.dto.auth.request.SignUpDTO;
import br.org.apae.api.common.exceptions.handler.GlobalExceptionHandler;
import br.org.apae.api.controllers.patient.PatientControllerImpl;
import br.org.apae.api.notification.domain.interfaces.EmailSender;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = { AuthControllerImpl.class, PatientControllerImpl.class })
@AutoConfigureMockMvc(addFilters = true)
@Import({
    SpringDataWebConfiguration.class,
    SecurityConfiguration.class,
    SecurityFilter.class,
    RestAuthenticationEntryPoint.class,
    RestAccessDeniedHandler.class,
    GlobalExceptionHandler.class,
    AuthControllerSecurityTest.ContextConfiguration.class
})
@Tag("auth")
@Tag("unit")
@Tag("controller")
class AuthControllerSecurityTest {

    private static final String SIGNUP_URL = "/auth/signup";
    private static final String SIGNIN_URL = "/auth/signin";
    private static final String NO_ACCESS_MESSAGE = "Usuário sem acesso ao APAE Geral.";
    private static final String PASSWORD = "Senha123";

    @TestConfiguration
    @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
    static class ContextConfiguration {

        @Bean
        AuthApplicationService authApplicationService(
                UserService userService,
                PasswordEncoder passwordEncoder,
                TokenProvider tokenProvider) throws Exception {
            AuthenticationManager authenticationManager = authentication -> new UsernamePasswordAuthenticationToken(
                    userService.findUserByUsername(authentication.getName()),
                    null,
                    List.of());
            AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
            when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

            return new AuthApplicationServiceImpl(
                    userService,
                    passwordEncoder,
                    authenticationConfiguration,
                    tokenProvider,
                    mock(PasswordRecoveryTokenRepository.class),
                    mock(EmailSender.class));
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PatientApplicationService patientService;

    @MockitoBean
    private DisorderApplicationService disorderApplicationService;

    @MockitoBean
    private AnnualRegistryApplicationService annualRegistryApplicationService;

    @MockitoBean
    private ServiceAreaApplicationService serviceAreaApplicationService;

    private static String usernameOf(UserRole role) {
        return role.name().toLowerCase() + "@apae.org.br";
    }

    private User userWithRole(UserRole role) {
        return new User(
                usernameOf(role),
                passwordEncoder.encode(PASSWORD),
                "12345678900",
                "Usuário " + role.name(),
                role);
    }

    private String bearerTokenFor(UserRole role) {
        String token = "token-" + role.name();
        when(jwtProvider.validateToken(token)).thenReturn(usernameOf(role));
        when(userService.findUserByUsername(usernameOf(role))).thenReturn(userWithRole(role));
        return "Bearer " + token;
    }

    private String signUpBody() throws Exception {
        return objectMapper.writeValueAsString(
                new SignUpDTO("joao@email.com", PASSWORD, "52998224725", "João Silva"));
    }

    private String signInBody(UserRole role) throws Exception {
        return objectMapper.writeValueAsString(new SignInDTO(usernameOf(role), PASSWORD));
    }

    @Nested
    @DisplayName("POST /auth/signup")
    class SignUpSecurityTests {

        @Test
        @DisplayName("Deve retornar Unauthorized (401) quando não houver token")
        void shouldReturnUnauthorizedWhenNoToken() throws Exception {
            mockMvc.perform(post(SIGNUP_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signUpBody()))
                    .andExpect(status().isUnauthorized());

            verify(userService, never()).createUser(anyString(), anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("Deve retornar Forbidden (403) quando o token for de usuário APAE_GERAL")
        void shouldReturnForbiddenWhenTokenIsApaeGeral() throws Exception {
            mockMvc.perform(post(SIGNUP_URL)
                            .header("Authorization", bearerTokenFor(UserRole.APAE_GERAL))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signUpBody()))
                    .andExpect(status().isForbidden());

            verify(userService, never()).createUser(anyString(), anyString(), anyString(), anyString());
        }

        @Test
        @DisplayName("Deve criar usuário (201) quando o token for de usuário ADMIN")
        void shouldCreateUserWhenTokenIsAdmin() throws Exception {
            mockMvc.perform(post(SIGNUP_URL)
                            .header("Authorization", bearerTokenFor(UserRole.ADMIN))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signUpBody()))
                    .andExpect(status().isCreated());

            verify(userService).createUser(eq("joao@email.com"), anyString(), eq("52998224725"), eq("João Silva"));
        }
    }

    @Nested
    @DisplayName("POST /auth/signin")
    class SignInSecurityTests {

        @Test
        @DisplayName("Deve retornar Forbidden (403) com mensagem para usuário GESTAO_ESCOLAR")
        void shouldReturnForbiddenForGestaoEscolar() throws Exception {
            when(userService.findUserByUsername(usernameOf(UserRole.GESTAO_ESCOLAR)))
                    .thenReturn(userWithRole(UserRole.GESTAO_ESCOLAR));

            mockMvc.perform(post(SIGNIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signInBody(UserRole.GESTAO_ESCOLAR)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(NO_ACCESS_MESSAGE));

            verify(jwtProvider, never()).generateToken(any(User.class));
        }

        @Test
        @DisplayName("Deve retornar Forbidden (403) com mensagem para usuário ATENDIMENTO")
        void shouldReturnForbiddenForAtendimento() throws Exception {
            when(userService.findUserByUsername(usernameOf(UserRole.ATENDIMENTO)))
                    .thenReturn(userWithRole(UserRole.ATENDIMENTO));

            mockMvc.perform(post(SIGNIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(signInBody(UserRole.ATENDIMENTO)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value(NO_ACCESS_MESSAGE));

            verify(jwtProvider, never()).generateToken(any(User.class));
        }
    }

    @Nested
    @DisplayName("Rotas de domínio por papel")
    class DomainRoutesSecurityTests {

        @ParameterizedTest(name = "GET {0} com token GESTAO_ESCOLAR deve retornar 403")
        @ValueSource(strings = {
                "/patients",
                "/professionals",
                "/appointments",
                "/patients/00000000-0000-0000-0000-000000000001/documents/medicals"
        })
        @DisplayName("Deve retornar Forbidden (403) nas listagens para GESTAO_ESCOLAR")
        void shouldReturnForbiddenForGestaoEscolarOnListings(String url) throws Exception {
            mockMvc.perform(get(url)
                            .header("Authorization", bearerTokenFor(UserRole.GESTAO_ESCOLAR))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve listar pacientes (200) com token APAE_GERAL")
        void shouldListPatientsForApaeGeral() throws Exception {
            when(patientService.findPatientByFilter(anyMap(), any())).thenReturn(new PageImpl<>(List.of()));

            mockMvc.perform(get("/patients")
                            .header("Authorization", bearerTokenFor(UserRole.APAE_GERAL))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)));
        }
    }
}
