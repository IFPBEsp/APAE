package br.org.apae.api.auth.infrastructure.security;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.org.apae.api.auth.domain.exceptions.TokenGenerationException;
import br.org.apae.api.auth.domain.exceptions.TokenVerificationException;
import br.org.apae.api.auth.domain.interfaces.TokenProvider;
import br.org.apae.api.auth.domain.model.User;

@Service
public class JwtProvider implements TokenProvider {
  @Value("${app.token.secret}")
  private String secret;

  @Value("${app.token.issuer}")
  private String ISSUER;

  @Value("${app.token.expiration-hours}")
  private long expirationHours;

  private Instant genExpirationDate() {
    return Instant.now().plus(Duration.ofHours(expirationHours));
  }

  @Override
  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.create()
          .withIssuer(ISSUER)
          .withSubject(user.getUsername())
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
      return JWT.require(algorithm)
          .withIssuer(ISSUER)
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new TokenVerificationException();
    }
  }
}
