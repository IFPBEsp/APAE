package br.org.apae.api.auth.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void createProfessionalUserShouldStartWithoutCredentials() {
    User user = User.createProfessionalUser(
        "profissional@apae.org.br",
        "Maria Profissional",
        "(11) 99999-9999",
        "123456789",
        null);

    assertEquals("profissional@apae.org.br", user.getUsername());
    assertEquals("Maria Profissional", user.getFullName());
    assertEquals("(11) 99999-9999", user.getPhoneNumber());
    assertEquals("123456789", user.getIdentityDocument());
    assertEquals(UserRole.ATENDIMENTO, user.getRole());
    assertNull(user.getCpf());
    assertNull(user.getPassword());
    assertFalse(user.hasConfiguredPassword());
  }
}
