package br.org.apae.api.notification.domain.interfaces;

import br.org.apae.api.notification.domain.model.EmailMessage;

public interface EmailSender {
  void send(EmailMessage emailMessage);
}