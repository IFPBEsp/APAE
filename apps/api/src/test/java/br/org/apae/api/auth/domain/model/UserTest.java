package br.org.apae.api.auth.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void createProfessionalUserShouldStartWithoutCredentials() {
    User user = User.createProfessionalUser(
        "profissional@apae.org.br",
        "123.456.789-09",
        "Maria Profissional",
        "(11) 99999-9999",
        "123456789",
        null);

    assertEquals("profissional@apae.org.br", user.getUsername());
    assertEquals("123.456.789-09", user.getCpf());
    assertEquals("Maria Profissional", user.getFullName());
    assertEquals("(11) 99999-9999", user.getPhoneNumber());
    assertEquals("123456789", user.getIdentityDocument());
    assertEquals(UserRole.ATENDIMENTO, user.getRole());
    assertNull(user.getPassword());
    assertFalse(user.hasConfiguredPassword());
    assertTrue(user.isFirstAccess());
  }
}
