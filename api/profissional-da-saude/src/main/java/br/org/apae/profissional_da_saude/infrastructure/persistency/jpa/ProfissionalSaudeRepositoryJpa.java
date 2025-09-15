package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.ProfissionalSaudeEntity;

import java.util.UUID;

public interface ProfissionalSaudeRepositoryJpa extends JpaRepository<ProfissionalSaudeEntity, UUID> {

    boolean existsByEmail(String email);
    boolean existsByDocProfissional(String docProfissional);
}
