package br.org.apae.api.notification.domain.model;

import java.util.List;

public class EmailMessage {
  private final List<String> to;
  private final String subject;
  private final String body;

  public EmailMessage(List<String> to, String subject, String body) {
    this.to = to;
    this.subject = subject;
    this.body = body;
  }

  public List<String> getTo() {
    return to;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }
}