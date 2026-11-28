package br.org.apae.api.auth.infrastructure.security;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier.BaseVerification;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.org.apae.api.auth.domain.exceptions.TokenGenerationException;
import br.org.apae.api.auth.domain.exceptions.TokenVerificationException;
import br.org.apae.api.auth.domain.interfaces.TokenProvider;
import br.org.apae.api.auth.domain.model.User;

@Service
public class JwtProvider implements TokenProvider {
  private final String secret;
  private final String issuer;
  private final long expirationHours;
  private final Clock clock;

  public JwtProvider(
      @Value("${app.token.secret}") String secret,
      @Value("${app.token.issuer}") String issuer,
      @Value("${app.token.expiration-hours}") long expirationHours,
      Clock clock) {
    this.secret = secret;
    this.issuer = issuer;
    this.expirationHours = expirationHours;
    this.clock = clock;
  }

  private Instant genExpirationDate() {
    return clock.instant().plus(Duration.ofHours(expirationHours));
  }

  @Override
  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer(issuer)
          .withSubject(user.getUsername())
          .withClaim("role", user.getRole().name())
          .withExpiresAt(genExpirationDate())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new TokenGenerationException();
    }
  }

  @Override
  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      BaseVerification verification = (BaseVerification) JWT.require(algorithm).withIssuer(issuer);
      return verification.build(clock)
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new TokenVerificationException();
    }
  }
}
