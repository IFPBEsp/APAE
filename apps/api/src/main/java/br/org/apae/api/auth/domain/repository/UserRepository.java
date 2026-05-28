package br.org.apae.api.auth.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api.auth.domain.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  Optional<User> findByCpf(String cpf);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIdNot(String email, UUID id);

  boolean existsByIdentityDocument(String identityDocument);

  boolean existsByIdentityDocumentAndIdNot(String identityDocument, UUID id);
}
