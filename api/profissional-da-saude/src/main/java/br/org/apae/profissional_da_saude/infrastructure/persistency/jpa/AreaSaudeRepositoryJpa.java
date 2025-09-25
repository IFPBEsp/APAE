package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.infrastructure.entity.AreaSaudeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaSaudeRepositoryJpa extends JpaRepository<AreaSaudeEntity, Integer> {
    boolean existsByArea(String area);
}
