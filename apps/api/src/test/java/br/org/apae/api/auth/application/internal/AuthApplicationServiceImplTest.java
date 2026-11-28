package br.org.apae.api.auth.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.org.apae.api.auth.domain.exceptions.AuthenticationException;
import br.org.apae.api.auth.domain.interfaces.TokenProvider;
import br.org.apae.api.auth.domain.model.PasswordRecoveryToken;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.domain.repository.PasswordRecoveryTokenRepository;
import br.org.apae.api.common.dto.auth.request.PasswordRecoveryRequestDTO;
import br.org.apae.api.common.dto.auth.request.PasswordResetDTO;
import br.org.apae.api.notification.domain.interfaces.EmailSender;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceImplTest {
  private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
  private static final Instant REQUESTED_AT = Instant.parse("2024-01-15T12:00:00Z");
  private static final Instant EXPIRATION_INSTANT = Instant.parse("2024-01-15T12:30:00Z");
  private static final LocalDateTime EXPIRES_AT = LocalDateTime.of(2024, 1, 15, 9, 30);
  private static final String EMAIL = "user@example.com";
  private static final String NEW_PASSWORD = "new-password";
  private static final PasswordResetDTO RESET = new PasswordResetDTO("recovery-token", NEW_PASSWORD, NEW_PASSWORD);

  @Mock
  private UserService userService;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private AuthenticationConfiguration authenticationConfiguration;
  @Mock
  private TokenProvider tokenProvider;
  @Mock
  private PasswordRecoveryTokenRepository tokenRepository;
  @Mock
  private EmailSender emailSender;

  private final User user = new User(EMAIL, "old-password-hash", "12345678900", "Test User", UserRole.ADMIN);

  @Test
  void setsRecoveryTokenExpirationToThirtyMinutesInTheClockZone() {
    when(userService.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

    serviceAt(REQUESTED_AT).requestPasswordRecovery(new PasswordRecoveryRequestDTO(EMAIL));

    ArgumentCaptor<PasswordRecoveryToken> token = ArgumentCaptor.forClass(PasswordRecoveryToken.class);
    verify(tokenRepository).save(token.capture());
    assertEquals(EXPIRES_AT, token.getValue().getExpiresAt());
  }

  @ParameterizedTest
  @ValueSource(longs = {-1, 0})
  void acceptsRecoveryTokenBeforeAndAtExpiration(long nanosFromExpiration) {
    PasswordRecoveryToken token = storedToken();
    when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn("new-password-hash");

    serviceAt(EXPIRATION_INSTANT.plusNanos(nanosFromExpiration)).resetPassword(RESET);

    assertEquals("new-password-hash", user.getPassword());
    assertTrue(token.isUsed());
    verify(userService).save(user);
    verify(tokenRepository).save(token);
  }

  @Test
  void rejectsRecoveryTokenAfterExpiration() {
    PasswordRecoveryToken token = storedToken();
    AuthApplicationServiceImpl service = serviceAt(EXPIRATION_INSTANT.plusNanos(1));

    AuthenticationException exception = assertThrows(AuthenticationException.class, () -> service.resetPassword(RESET));

    assertEquals("Token expirado.", exception.getMessage());
    assertFalse(token.isUsed());
    verifyNoInteractions(passwordEncoder, userService);
    verify(tokenRepository, never()).save(any());
  }

  private PasswordRecoveryToken storedToken() {
    PasswordRecoveryToken token = new PasswordRecoveryToken("token-hash", user, EXPIRES_AT);
    when(tokenRepository.findByTokenHash(any())).thenReturn(Optional.of(token));
    return token;
  }

  private AuthApplicationServiceImpl serviceAt(Instant instant) {
    return new AuthApplicationServiceImpl(userService, passwordEncoder, authenticationConfiguration, tokenProvider,
        tokenRepository, emailSender, Clock.fixed(instant, ZONE));
  }
}
