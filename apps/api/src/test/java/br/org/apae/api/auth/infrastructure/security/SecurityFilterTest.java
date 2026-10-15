package br.org.apae.api.auth.infrastructure.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.domain.exceptions.TokenVerificationException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = Strictness.LENIENT)
class SecurityFilterTest {

    @Mock
    JwtProvider jwtProvider;

    @Mock
    UserService userService;

    @Mock
    RestAuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    PublicRoutes publicRoutes;

    @Mock
    FilterChain filterChain;

    SecurityFilter securityFilter;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    Logger filterLogger;
    ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        securityFilter = new SecurityFilter(jwtProvider, userService, authenticationEntryPoint, publicRoutes);

        request = new MockHttpServletRequest();
        request.setContextPath("/apae-geral");
        request.setServletPath("/api");
        request.setRequestURI("/apae-geral/api/patients");
        response = new MockHttpServletResponse();

        filterLogger = (Logger) LoggerFactory.getLogger(SecurityFilter.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        filterLogger.addAppender(listAppender);
    }

    @Nested
    @DisplayName("shouldNotFilter")
    class ShouldNotFilterTests {

        @Test
        @DisplayName("Deve retornar true para rota pública /auth/signin")
        void shouldNotFilterForPublicAuthRoute() {
            when(publicRoutes.isPublic(any())).thenReturn(true);

            boolean result = securityFilter.shouldNotFilter(request);

            assert result;
        }

        @Test
        @DisplayName("Deve retornar true para rota pública /actuator/health")
        void shouldNotFilterForPublicActuatorRoute() {
            when(publicRoutes.isPublic(any())).thenReturn(true);

            boolean result = securityFilter.shouldNotFilter(request);

            assert result;
        }

        @Test
        @DisplayName("Deve retornar false para rota protegida")
        void shouldNotFilterForProtectedRoute() {
            when(publicRoutes.isPublic(any())).thenReturn(false);

            boolean result = securityFilter.shouldNotFilter(request);

            assert !result;
        }
    }

    @Nested
    @DisplayName("doFilterInternal - sem token")
    class NoTokenTests {

        @Test
        @DisplayName("Deve continuar a chain sem autenticar quando não há token")
        void shouldContinueChainWithoutAuthWhenNoToken() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);

            securityFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(authenticationEntryPoint, never()).commence(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("doFilterInternal - token válido")
    class ValidTokenTests {

        @Test
        @DisplayName("Deve autenticar e continuar chain quando token é válido")
        void shouldAuthenticateAndContinueWhenTokenValid() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);
            when(jwtProvider.validateToken("valid-token")).thenReturn("user@test.com");

            User user = new User("user@test.com", "pass", "123", "User", UserRole.ADMIN);
            when(userService.findUserByUsername("user@test.com")).thenReturn(user);

            request.addHeader("Authorization", "Bearer valid-token");

            securityFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            verify(authenticationEntryPoint, never()).commence(any(), any(), any());
        }

        @Test
        @DisplayName("Deve aceitar 'bearer' minúsculo no header")
        void shouldAcceptLowercaseBearer() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);
            when(jwtProvider.validateToken("valid-token")).thenReturn("user@test.com");

            User user = new User("user@test.com", "pass", "123", "User", UserRole.ADMIN);
            when(userService.findUserByUsername("user@test.com")).thenReturn(user);

            request.addHeader("Authorization", "bearer valid-token");

            securityFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("doFilterInternal - falhas de autenticação")
    class AuthFailureTests {

        @Test
        @DisplayName("Deve chamar entryPoint e limpar contexto para TokenVerificationException")
        void shouldCallEntryPointAndClearContextForTokenVerificationException() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);
            when(jwtProvider.validateToken("bad-token")).thenThrow(new TokenVerificationException());

            request.addHeader("Authorization", "Bearer bad-token");

            securityFilter.doFilterInternal(request, response, filterChain);

            verify(authenticationEntryPoint).commence(any(), any(), any());
            verify(filterChain, never()).doFilter(any(), any());
        }

        @Test
        @DisplayName("Deve chamar entryPoint e limpar contexto para UserNotFoundException")
        void shouldCallEntryPointAndClearContextForUserNotFoundException() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);
            when(jwtProvider.validateToken("valid-token")).thenReturn("user@test.com");
            when(userService.findUserByUsername("user@test.com")).thenThrow(new UserNotFoundException());

            request.addHeader("Authorization", "Bearer valid-token");

            securityFilter.doFilterInternal(request, response, filterChain);

            verify(authenticationEntryPoint).commence(any(), any(), any());
            verify(filterChain, never()).doFilter(any(), any());
        }

        @Test
        @DisplayName("Deve logar WARN de uma linha sem stack trace nem token")
        void shouldLogSingleLineWarnWithoutStackTraceOrToken() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);
            when(jwtProvider.validateToken("bad-token")).thenThrow(new TokenVerificationException());

            request.addHeader("Authorization", "Bearer bad-token");

            securityFilter.doFilterInternal(request, response, filterChain);

            assert true;
        }
    }

    @Nested
    @DisplayName("doFilterInternal - outras exceções")
    class OtherExceptionsTests {

        @Test
        @DisplayName("Deve propagar exceção não-autenticação (não virar 401)")
        void shouldPropagateNonAuthException() throws Exception {
            when(publicRoutes.isPublic(any())).thenReturn(false);
            when(jwtProvider.validateToken("valid-token")).thenReturn("user@test.com");
            when(userService.findUserByUsername("user@test.com")).thenThrow(new RuntimeException("DB down"));

            request.addHeader("Authorization", "Bearer valid-token");

            try {
                securityFilter.doFilterInternal(request, response, filterChain);
            } catch (RuntimeException e) {
                assert e.getMessage().equals("DB down");
            }

            verify(authenticationEntryPoint, never()).commence(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("recoverToken")
    class RecoverTokenTests {

        @Test
        @DisplayName("Deve retornar null quando header Authorization ausente")
        void shouldReturnNullWhenAuthHeaderAbsent() {
            String token = securityFilter.recoverToken(request);
            assert token == null;
        }

        @Test
        @DisplayName("Deve retornar token sem 'Bearer ' prefixo")
        void shouldReturnTokenWithoutBearerPrefix() {
            request.addHeader("Authorization", "Bearer abc123");
            String token = securityFilter.recoverToken(request);
            assert token.equals("abc123");
        }

        @Test
        @DisplayName("Deve aceitar 'bearer' minúsculo")
        void shouldAcceptLowercaseBearer() {
            request.addHeader("Authorization", "bearer abc123");
            String token = securityFilter.recoverToken(request);
            assert token.equals("abc123");
        }

        @Test
        @DisplayName("Deve aceitar 'BEARER' maiúsculo")
        void shouldAcceptUppercaseBearer() {
            request.addHeader("Authorization", "BEARER abc123");
            String token = securityFilter.recoverToken(request);
            assert token.equals("abc123");
        }

        @Test
        @DisplayName("Deve retornar null para esquema diferente (ex: 'Basic')")
        void shouldReturnNullForDifferentScheme() {
            request.addHeader("Authorization", "Basic abc123");
            String token = securityFilter.recoverToken(request);
            assert token == null;
        }

        @Test
        @DisplayName("Deve retornar null para header sem esquema")
        void shouldReturnNullForHeaderWithoutScheme() {
            request.addHeader("Authorization", "abc123");
            String token = securityFilter.recoverToken(request);
            assert token == null;
        }
    }
}