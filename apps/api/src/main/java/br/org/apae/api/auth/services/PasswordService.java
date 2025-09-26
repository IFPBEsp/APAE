package br.org.apae.api.auth.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
  private final PasswordEncoder encoder;

  public PasswordService(PasswordEncoder encoder) {
    this.encoder = encoder;
  }

  public String hashPassword(String rawPassword) {
    return encoder.encode(rawPassword);
  }

  public boolean matches(String rawPassword, String passwordEncoded) {
    return encoder.matches(rawPassword, passwordEncoded);
  }
}
