package br.org.apae.api.auth.application.internal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.apae.api.auth.application.interfaces.AuthApplicationService;
import br.org.apae.api.auth.domain.exceptions.AuthenticationException;
import br.org.apae.api.auth.domain.exceptions.InvalidPasswordException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.interfaces.dto.SignInDTO;
import br.org.apae.api.auth.interfaces.dto.SignUpDTO;
import br.org.apae.api.auth.interfaces.dto.TokenResponseDTO;

@Service
public class AuthApplicationServiceImpl implements AuthApplicationService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final AuthenticationConfiguration authenticationConfiguration;

  public AuthApplicationServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
      AuthenticationConfiguration authenticationConfiguration,
      JwtProvider jwtProvider) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.authenticationConfiguration = authenticationConfiguration;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void signUp(SignUpDTO signUpDto) {
    String passwordHashed = passwordEncoder.encode(signUpDto.password());
    userService.createUser(signUpDto.username(), passwordHashed);
  }

  @Override
  public TokenResponseDTO signIn(SignInDTO signInDto) {
    try {
      User user = userService.findUserByUsername(signInDto.username());

      if (!passwordEncoder.matches(signInDto.password(), user.getPassword())) {
        throw new InvalidPasswordException();
      }

      AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
      var authenticationToken = new UsernamePasswordAuthenticationToken(
          signInDto.username(), signInDto.password());
      var authentication = authenticationManager.authenticate(authenticationToken);

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String jwt = jwtProvider.generateToken((User) userDetails);

      return new TokenResponseDTO(jwt);
    } catch (UserNotFoundException | InvalidPasswordException e) {
      throw e;
    } catch (Exception e) {
      throw new AuthenticationException();
    }
  }
}
