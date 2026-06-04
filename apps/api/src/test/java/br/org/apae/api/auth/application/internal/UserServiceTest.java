package br.org.apae.api.auth.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.org.apae.api.auth.domain.exceptions.UserConflictException;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.domain.repository.UserRepository;

class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository);
  }

  @Test
  void createUserShouldRejectDuplicatedEmail() {
    when(userRepository.existsByEmail("admin@apae.org.br")).thenReturn(true);

    assertThrows(UserConflictException.class, () -> userService.createUser(
        "admin@apae.org.br",
        "encoded-password",
        "12345678900",
        "Administrador"));

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void createUserShouldRejectDuplicatedCpf() {
    when(userRepository.existsByEmail("admin@apae.org.br")).thenReturn(false);
    when(userRepository.existsByCpf("12345678900")).thenReturn(true);

    assertThrows(UserConflictException.class, () -> userService.createUser(
        "admin@apae.org.br",
        "encoded-password",
        "12345678900",
        "Administrador"));

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void createUserShouldSaveAuthenticatedAtendimentoUser() {
    when(userRepository.existsByEmail("admin@apae.org.br")).thenReturn(false);
    when(userRepository.existsByCpf("12345678900")).thenReturn(false);

    userService.createUser(
        "admin@apae.org.br",
        "encoded-password",
        "12345678900",
        "Administrador");

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("admin@apae.org.br", savedUser.getUsername());
    assertEquals("encoded-password", savedUser.getPassword());
    assertEquals("12345678900", savedUser.getCpf());
    assertEquals("Administrador", savedUser.getFullName());
    assertEquals(UserRole.ATENDIMENTO, savedUser.getRole());
  }
}
