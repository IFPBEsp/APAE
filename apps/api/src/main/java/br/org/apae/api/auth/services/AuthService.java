package br.org.apae.api.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.org.apae.api.auth.config.security.TokenService;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.dto.SignInDTO;
import br.org.apae.api.auth.dto.SignUpDTO;
import br.org.apae.api.auth.dto.TokenResponseDTO;
import br.org.apae.api.auth.exceptions.messages.ExceptionMessage;
import br.org.apae.api.auth.exceptions.types.AuthenticationException;
import br.org.apae.api.auth.facade.IAuthFacade;

@Service
public class AuthService implements UserDetailsService, IAuthFacade {

  private final UserService userService;
  private final PasswordService passwordService;
  private final TokenService tokenService;
  private final AuthenticationConfiguration authenticationConfiguration;

  public AuthService(UserService userService, PasswordService passwordService,
      AuthenticationConfiguration authenticationConfiguration,
      TokenService tokenService) {
    this.userService = userService;
    this.passwordService = passwordService;
    this.authenticationConfiguration = authenticationConfiguration;
    this.tokenService = tokenService;
  }

  @Override
  public void signUp(SignUpDTO signUpDto) {
    String passwordHashed = passwordService.hashPassword(signUpDto.password());
    userService.createUser(signUpDto.username(), passwordHashed);
  }

  @Override
  public TokenResponseDTO signIn(SignInDTO signInDto) {
    try {
      AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
      var authenticationToken = new UsernamePasswordAuthenticationToken(
          signInDto.username(), signInDto.password());

      var authentication = authenticationManager.authenticate(authenticationToken);

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String jwt = tokenService.generateToken((User) userDetails);

      return new TokenResponseDTO(jwt);
    } catch (Exception e) {
      throw new AuthenticationException(ExceptionMessage.AUTHENTICATION_FAILED, e);
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userService.findUserByUsername(username);
  }
}
