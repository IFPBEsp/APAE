package br.org.apae.api.profissional.da.saude.domain.repository;

import br.org.apae.api.profissional.da.saude.infra.entity.ProfissionalSaudeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaudeEntity, UUID> {

    Page<ProfissionalSaudeEntity> findAll(Pageable pageable);

    boolean existsByDocProfissional(String docProfissional);

    boolean existsByEmail(String email);
}
