package br.org.apae.api.controllers.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.org.apae.api.auth.application.interfaces.AuthApplicationService;
import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.domain.exceptions.AuthenticationException;
import br.org.apae.api.auth.domain.exceptions.InvalidPasswordException;
import br.org.apae.api.auth.domain.exceptions.UserConflictException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.common.dto.auth.request.PasswordRecoveryRequestDTO;
import br.org.apae.api.common.dto.auth.request.PasswordResetDTO;
import br.org.apae.api.common.dto.auth.request.SignInDTO;
import br.org.apae.api.common.dto.auth.request.SignUpDTO;
import br.org.apae.api.common.dto.auth.response.TokenResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@Tag("auth")
@Tag("unit")
@Tag("controller")
class AuthControllerImplTest {

    private static final String BASE_URL = "/auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthApplicationService authService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserService userService;

    @Nested
    @DisplayName("POST /auth/signup")
    class SignUpTests {

        @Test
        @DisplayName("Deve criar usuário com sucesso (201)")
        void shouldSignUpSuccessfully() throws Exception {
            SignUpDTO requestDto = new SignUpDTO(
                    "joao@email.com",
                    "Senha123",
                    "12345678901",
                    "João Silva"
            );

            mockMvc.perform(post(BASE_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated());

            verify(authService).signUp(requestDto);
        }

        @Test
        @DisplayName("Deve retornar Conflict (409) ao tentar cadastrar e-mail duplicado")
        void shouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
            SignUpDTO requestDto = new SignUpDTO(
                    "joao@email.com",
                    "Senha123",
                    "12345678901",
                    "João Silva"
            );

            doThrow(new UserConflictException())
                    .when(authService)
                    .signUp(requestDto);

            mockMvc.perform(post(BASE_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isConflict());

            verify(authService).signUp(requestDto);
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando JSON estiver malformado")
        void shouldReturnBadRequestWhenSignUpJsonIsMalformed() throws Exception {
            String malformedJson = """
                    {
                      "email": "joao@email.com",
                      "password": "Senha123",
                    }
                    """;

            mockMvc.perform(post(BASE_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(malformedJson))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).signUp(any(SignUpDTO.class));
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando dados de cadastro forem inválidos")
        void shouldReturnBadRequestWhenSignUpPayloadIsInvalid() throws Exception {
            SignUpDTO requestDto = new SignUpDTO(
                    "email-invalido",
                    "",
                    "",
                    ""
            );

            mockMvc.perform(post(BASE_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .signUp(any(SignUpDTO.class));
        }
    }

    @Nested
    @DisplayName("POST /auth/signin")
    class SignInTests {

        @Test
        @DisplayName("Deve autenticar usuário com sucesso (200)")
        void shouldSignInSuccessfully() throws Exception {
            SignInDTO requestDto = new SignInDTO(
                    "joao@email.com",
                    "Senha123"
            );

            TokenResponseDTO responseDto = new TokenResponseDTO("jwt-token");

            when(authService.signIn(requestDto)).thenReturn(responseDto);

            mockMvc.perform(post(BASE_URL + "/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token"));

            verify(authService).signIn(requestDto);
        }

        @Test
        @DisplayName("Deve retornar Unauthorized (401) quando credenciais forem inválidas")
        void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
            SignInDTO requestDto = new SignInDTO(
                    "joao@email.com",
                    "senha-incorreta"
            );

            when(authService.signIn(requestDto))
                    .thenThrow(new InvalidPasswordException());

            mockMvc.perform(post(BASE_URL + "/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isUnauthorized());

            verify(authService).signIn(requestDto);
        }

        @Test
        @DisplayName("Deve retornar Unauthorized (401) quando usuário não existir")
        void shouldReturnUnauthorizedWhenUserDoesNotExist() throws Exception {
            SignInDTO requestDto = new SignInDTO(
                    "inexistente@email.com",
                    "Senha123"
            );

            when(authService.signIn(requestDto))
                    .thenThrow(new UserNotFoundException());

            mockMvc.perform(post(BASE_URL + "/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isUnauthorized());

            verify(authService).signIn(requestDto);
        }
    }

    @Nested
    @DisplayName("POST /auth/password-recovery/request")
    class PasswordRecoveryTests {

        @Test
        @DisplayName("Deve solicitar recuperação de senha com sucesso (200)")
        void shouldRequestPasswordRecoverySuccessfully() throws Exception {
            PasswordRecoveryRequestDTO requestDto =
                    new PasswordRecoveryRequestDTO("joao@email.com");

            mockMvc.perform(post(BASE_URL + "/password-recovery/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message")
                            .value("Se o e-mail estiver cadastrado, as instruções de recuperação foram enviadas."));

            verify(authService).requestPasswordRecovery(requestDto);
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando e-mail for inválido")
        void shouldReturnBadRequestWhenPasswordRecoveryEmailIsInvalid() throws Exception {
            PasswordRecoveryRequestDTO requestDto =
                    new PasswordRecoveryRequestDTO("email-invalido");

            mockMvc.perform(post(BASE_URL + "/password-recovery/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .requestPasswordRecovery(any(PasswordRecoveryRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando e-mail estiver vazio")
        void shouldReturnBadRequestWhenPasswordRecoveryEmailIsBlank() throws Exception {
            PasswordRecoveryRequestDTO requestDto =
                    new PasswordRecoveryRequestDTO("");

            mockMvc.perform(post(BASE_URL + "/password-recovery/request")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .requestPasswordRecovery(any(PasswordRecoveryRequestDTO.class));
        }
    }

    @Nested
    @DisplayName("POST /auth/password-recovery/reset")
    class PasswordResetTests {

        @Test
        @DisplayName("Deve redefinir senha com sucesso (200)")
        void shouldResetPasswordSuccessfully() throws Exception {
            PasswordResetDTO requestDto = new PasswordResetDTO(
                    "valid-token",
                    "NovaSenha123",
                    "NovaSenha123"
            );

            mockMvc.perform(post(BASE_URL + "/password-recovery/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Senha redefinida com sucesso."));

            verify(authService).resetPassword(requestDto);
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando token for inválido")
        void shouldReturnBadRequestWhenPasswordResetTokenIsInvalid() throws Exception {
            PasswordResetDTO requestDto = new PasswordResetDTO(
                    "invalid-token",
                    "NovaSenha123",
                    "NovaSenha123"
            );

            doThrow(new AuthenticationException("Token inválido."))
                    .when(authService)
                    .resetPassword(requestDto);

            mockMvc.perform(post(BASE_URL + "/password-recovery/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Token inválido."));

            verify(authService).resetPassword(requestDto);
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando token estiver vazio")
        void shouldReturnBadRequestWhenPasswordResetTokenIsBlank() throws Exception {
            PasswordResetDTO requestDto = new PasswordResetDTO(
                    "",
                    "NovaSenha123",
                    "NovaSenha123"
            );

            mockMvc.perform(post(BASE_URL + "/password-recovery/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .resetPassword(any(PasswordResetDTO.class));
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando nova senha estiver vazia")
        void shouldReturnBadRequestWhenNewPasswordIsBlank() throws Exception {
            PasswordResetDTO requestDto = new PasswordResetDTO(
                    "valid-token",
                    "",
                    "NovaSenha123"
            );

            mockMvc.perform(post(BASE_URL + "/password-recovery/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .resetPassword(any(PasswordResetDTO.class));
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando nova senha tiver menos de 6 caracteres")
        void shouldReturnBadRequestWhenNewPasswordHasLessThanSixCharacters() throws Exception {
            PasswordResetDTO requestDto = new PasswordResetDTO(
                    "valid-token",
                    "12345",
                    "12345"
            );

            mockMvc.perform(post(BASE_URL + "/password-recovery/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .resetPassword(any(PasswordResetDTO.class));
        }

        @Test
        @DisplayName("Deve retornar BadRequest (400) quando confirmação de senha estiver vazia")
        void shouldReturnBadRequestWhenConfirmPasswordIsBlank() throws Exception {
            PasswordResetDTO requestDto = new PasswordResetDTO(
                    "valid-token",
                    "NovaSenha123",
                    ""
            );

            mockMvc.perform(post(BASE_URL + "/password-recovery/reset")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest());

            verify(authService, never())
                    .resetPassword(any(PasswordResetDTO.class));
        }
    }
}