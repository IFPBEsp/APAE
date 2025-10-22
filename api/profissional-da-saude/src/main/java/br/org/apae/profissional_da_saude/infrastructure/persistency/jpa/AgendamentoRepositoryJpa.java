package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface AgendamentoRepositoryJpa extends JpaRepository<AgendamentoEntity, UUID> {
}
