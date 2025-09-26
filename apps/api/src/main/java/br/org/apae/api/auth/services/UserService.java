package br.org.apae.api.auth.services;

import org.springframework.stereotype.Service;

import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.domain.repository.UserRepository;
import br.org.apae.api.auth.exceptions.types.InvalidCredentialsException;
import br.org.apae.api.auth.exceptions.types.UserNotFoundException;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createUser(String username, String password) {
    boolean exists = userRepository.existsByUsername(username);

    if (exists) {
      throw new InvalidCredentialsException();
    }

    User user = new User(username, password, UserRole.ADMIN);
    userRepository.save(user);
  }

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
  }
}
