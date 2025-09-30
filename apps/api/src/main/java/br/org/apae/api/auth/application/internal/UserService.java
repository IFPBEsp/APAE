package br.org.apae.api.auth.application.internal;

import org.springframework.stereotype.Service;

import br.org.apae.api.auth.domain.exceptions.UserConflictException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.domain.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createUser(String username, String password) {
    boolean exists = userRepository.existsByUsername(username);

    if (exists) {
      throw new UserConflictException();
    }

    User user = new User(username, password, UserRole.ADMIN);
    userRepository.save(user);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
  }
}
