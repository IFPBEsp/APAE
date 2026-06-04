package br.org.apae.api.auth.application.internal;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import br.org.apae.api.auth.domain.exceptions.UserConflictException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.domain.repository.UserRepository;

@Service
public class UserService {
  private static final Pattern CPF_PATTERN = Pattern.compile(
      "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");
  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[a-zA-Z0-9].[a-zA-Z0-9\\._%\\+\\-]{0,63}@[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,30}");

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createUser(String email, String password, String cpf, String fullName) {
    if (userRepository.existsByEmail(email)) {
      throw new UserConflictException();
    }

    if (cpf != null && !cpf.isBlank() && userRepository.existsByCpf(cpf)) {
      throw new UserConflictException();
    }

    User user = User.createAuthenticatedUser(email, password, cpf, fullName, UserRole.ATENDIMENTO);
    userRepository.save(user);
  }

  public User findUserByUsername(String username) {
    if (CPF_PATTERN.matcher(username).matches()) {
      return userRepository.findByCpf(username).orElseThrow(UserNotFoundException::new);
    }

    if (EMAIL_PATTERN.matcher(username).matches()) {
      return userRepository.findByEmail(username).orElseThrow(UserNotFoundException::new);
    }

    throw new UserNotFoundException();
  }

  public Optional<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User save(User user) {
    return userRepository.save(user);
  }
}
