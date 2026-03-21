package br.org.apae.api.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "password_recovery_token")
public class PasswordRecoveryToken {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "token_hash", nullable = false, unique = true)
  private String tokenHash;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false)
  private boolean used;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "used_at")
  private LocalDateTime usedAt;

  protected PasswordRecoveryToken() {
  }

  public PasswordRecoveryToken(String tokenHash, User user, LocalDateTime expiresAt) {
    this.tokenHash = tokenHash;
    this.user = user;
    this.expiresAt = expiresAt;
    this.used = false;
  }

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getTokenHash() {
    return tokenHash;
  }

  public User getUser() {
    return user;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public boolean isUsed() {
    return used;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUsedAt() {
    return usedAt;
  }

  public void markAsUsed() {
    this.used = true;
    this.usedAt = LocalDateTime.now();
  }
}