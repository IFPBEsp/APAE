package br.org.apae.api.auth.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api.auth.domain.model.PasswordRecoveryToken;

public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, UUID> {
  Optional<PasswordRecoveryToken> findByTokenHash(String tokenHash);
}