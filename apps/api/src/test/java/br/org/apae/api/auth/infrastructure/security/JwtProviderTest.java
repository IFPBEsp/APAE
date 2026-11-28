package br.org.apae.api.auth.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.org.apae.api.auth.domain.exceptions.TokenVerificationException;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;

class JwtProviderTest {
  private static final String SECRET = "jwt-test-secret";
  private static final String ISSUER = "apae-api";
  private static final String SUBJECT = "user@example.com";
  private static final long EXPIRATION_HOURS = 3;
  private static final Instant ISSUED_AT = Instant.parse("2024-01-15T12:00:00Z");
  private static final Instant EXPIRES_AT = Instant.parse("2024-01-15T15:00:00Z");

  private final User user = new User(SUBJECT, "encoded-password", "12345678900", "Test User", UserRole.ADMIN);
  private final JwtProvider provider = providerAt(ISSUED_AT);

  @Test
  void generatesTokenWithExpectedClaimsAndConfiguredDuration() {
    DecodedJWT token = JWT.decode(provider.generateToken(user));

    assertEquals(SUBJECT, token.getSubject());
    assertEquals(ISSUER, token.getIssuer());
    assertEquals("ADMIN", token.getClaim("role").asString());
    assertEquals(Duration.ofHours(EXPIRATION_HOURS), Duration.between(ISSUED_AT, token.getExpiresAtAsInstant()));
  }

  @Test
  void validatesTokenBeforeExpiration() {
    String token = provider.generateToken(user);

    assertEquals(SUBJECT, providerAt(EXPIRES_AT.minusSeconds(1)).validateToken(token));
  }

  @ParameterizedTest
  @ValueSource(longs = {0, 1})
  void rejectsTokenAtOrAfterExpiration(long secondsAfterExpiration) {
    String token = provider.generateToken(user);
    JwtProvider validator = providerAt(EXPIRES_AT.plusSeconds(secondsAfterExpiration));

    assertThrows(TokenVerificationException.class, () -> validator.validateToken(token));
  }

  @Test
  void rejectsTokenSignedWithAnotherKey() {
    JwtProvider otherSigner = new JwtProvider("different-test-secret", ISSUER, EXPIRATION_HOURS,
        Clock.fixed(ISSUED_AT, ZoneOffset.UTC));
    String token = otherSigner.generateToken(user);

    assertThrows(TokenVerificationException.class, () -> provider.validateToken(token));
  }

  @Test
  void rejectsTokenWithAnotherIssuer() {
    JwtProvider otherIssuer = new JwtProvider(SECRET, "other-issuer", EXPIRATION_HOURS,
        Clock.fixed(ISSUED_AT, ZoneOffset.UTC));
    String token = otherIssuer.generateToken(user);

    assertThrows(TokenVerificationException.class, () -> provider.validateToken(token));
  }

  private JwtProvider providerAt(Instant instant) {
    return new JwtProvider(SECRET, ISSUER, EXPIRATION_HOURS, Clock.fixed(instant, ZoneOffset.UTC));
  }
}
