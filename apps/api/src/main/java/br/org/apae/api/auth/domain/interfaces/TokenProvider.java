package br.org.apae.api.auth.domain.interfaces;

import br.org.apae.api.auth.domain.model.User;

public interface TokenProvider {
  String generateToken(User user);

  String validateToken(String token);
}
