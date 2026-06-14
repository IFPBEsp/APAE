package br.org.apae.api.auth.application.internal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.auth.application.interfaces.AuthApplicationService;
import br.org.apae.api.auth.domain.exceptions.AuthenticationException;
import br.org.apae.api.auth.domain.exceptions.InvalidPasswordException;
import br.org.apae.api.auth.domain.exceptions.UserNotFoundException;
import br.org.apae.api.auth.domain.interfaces.TokenProvider;
import br.org.apae.api.auth.domain.model.PasswordRecoveryToken;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.repository.PasswordRecoveryTokenRepository;
import br.org.apae.api.common.dto.auth.request.PasswordRecoveryRequestDTO;
import br.org.apae.api.common.dto.auth.request.PasswordResetDTO;
import br.org.apae.api.common.dto.auth.request.SignInDTO;
import br.org.apae.api.common.dto.auth.request.SignUpDTO;
import br.org.apae.api.common.dto.auth.response.TokenResponseDTO;
import br.org.apae.api.notification.domain.interfaces.EmailSender;
import br.org.apae.api.notification.domain.model.EmailMessage;

@Service
public class AuthApplicationServiceImpl implements AuthApplicationService {
  private static final int PASSWORD_RECOVERY_EXPIRATION_MINUTES = 30;

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
  private final EmailSender emailSender;

  @Value("${app.frontend.reset-password-url}")
  private String resetPasswordUrl;

  public AuthApplicationServiceImpl(
      UserService userService,
      PasswordEncoder passwordEncoder,
      AuthenticationConfiguration authenticationConfiguration,
      TokenProvider tokenProvider,
      PasswordRecoveryTokenRepository passwordRecoveryTokenRepository,
      EmailSender emailSender) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.authenticationConfiguration = authenticationConfiguration;
    this.tokenProvider = tokenProvider;
    this.passwordRecoveryTokenRepository = passwordRecoveryTokenRepository;
    this.emailSender = emailSender;
  }

  @Override
  public void signUp(SignUpDTO signUpDto) {
    String passwordHashed = passwordEncoder.encode(signUpDto.password());
    userService.createUser(signUpDto.email(), passwordHashed, signUpDto.cpf(), signUpDto.fullName());
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
      String token = tokenProvider.generateToken((User) userDetails);

      return new TokenResponseDTO(token);
    } catch (UserNotFoundException | InvalidPasswordException e) {
        throw new InvalidPasswordException();
    } catch (Exception e) {
      throw new AuthenticationException();
    }
  }

  @Override
  @Transactional
  public void requestPasswordRecovery(PasswordRecoveryRequestDTO dto) {
    Optional<User> optionalUser = userService.findUserByEmail(dto.email());

    if (optionalUser.isEmpty()) {
      return;
    }

    User user = optionalUser.get();

    String rawToken = UUID.randomUUID().toString() + UUID.randomUUID();
    String tokenHash = hashToken(rawToken);

    PasswordRecoveryToken passwordRecoveryToken = new PasswordRecoveryToken(
        tokenHash,
        user,
        LocalDateTime.now().plusMinutes(PASSWORD_RECOVERY_EXPIRATION_MINUTES));

    passwordRecoveryTokenRepository.save(passwordRecoveryToken);

    String recoveryLink = resetPasswordUrl + "?token=" + rawToken;

    EmailMessage emailMessage = new EmailMessage(
        List.of(dto.email()),
        "Recuperação de senha",
        "Olá,\n\n" +
            "Recebemos uma solicitação para redefinição da sua senha.\n" +
            "Clique no link abaixo para continuar:\n\n" +
            recoveryLink +
            "\n\nSe você não solicitou esta alteração, ignore este e-mail.");

    emailSender.send(emailMessage);
  }

  @Override
  @Transactional
  public void resetPassword(PasswordResetDTO dto) {
    if (!dto.newPassword().equals(dto.confirmPassword())) {
      throw new AuthenticationException("As senhas não coincidem.");
    }

    String tokenHash = hashToken(dto.token());

    PasswordRecoveryToken recoveryToken = passwordRecoveryTokenRepository.findByTokenHash(tokenHash)
        .orElseThrow(() -> new AuthenticationException("Token inválido."));

    if (recoveryToken.isUsed()) {
      throw new AuthenticationException("Token já utilizado.");
    }

    if (recoveryToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new AuthenticationException("Token expirado.");
    }

    User user = recoveryToken.getUser();
    String encodedPassword = passwordEncoder.encode(dto.newPassword());

    user.updatePassword(encodedPassword);
    userService.save(user);

    recoveryToken.markAsUsed();
    passwordRecoveryTokenRepository.save(recoveryToken);
  }

  private String hashToken(String rawToken) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashBytes);
    } catch (Exception e) {
      throw new AuthenticationException("Erro ao processar token de recuperação.", e);
    }
  }
}