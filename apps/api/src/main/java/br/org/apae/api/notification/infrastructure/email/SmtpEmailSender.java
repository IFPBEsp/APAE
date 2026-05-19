package br.org.apae.api.notification.infrastructure.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.org.apae.api.notification.domain.exceptions.EmailSendingException;
import br.org.apae.api.notification.domain.interfaces.EmailSender;
import br.org.apae.api.notification.domain.model.EmailMessage;

@Service
public class SmtpEmailSender implements EmailSender {

  private static final Logger logger = LoggerFactory.getLogger(SmtpEmailSender.class);

  private final JavaMailSender mailSender;

  @Value("${spring.mail.host:}")
  private String mailHost;

  @Value("${spring.mail.port:0}")
  private int mailPort;

  @Value("${spring.mail.username:}")
  private String mailUsername;

  @Value("${spring.mail.password:}")
  private String mailPassword;

  public SmtpEmailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public void send(EmailMessage emailMessage) {
    if (!isConfigured()) {
      logger.info("SMTP não configurado. Envio de e-mail ignorado.");
      return;
    }

    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(emailMessage.getTo().toArray(new String[0]));
      message.setSubject(emailMessage.getSubject());
      message.setText(emailMessage.getBody());

      mailSender.send(message);
    } catch (Exception e) {
      throw new EmailSendingException("Não foi possível enviar o e-mail de recuperação de senha.", e);
    }
  }

  private boolean isConfigured() {
    return mailHost != null && !mailHost.isBlank()
        && mailPort > 0
        && mailUsername != null && !mailUsername.isBlank()
        && mailPassword != null && !mailPassword.isBlank();
  }
}