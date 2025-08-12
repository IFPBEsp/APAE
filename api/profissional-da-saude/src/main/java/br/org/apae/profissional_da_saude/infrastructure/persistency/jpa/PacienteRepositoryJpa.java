package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.infrastructure.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PacienteRepositoryJpa extends JpaRepository<PacienteEntity, UUID> {
}
