package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;

public interface AgendamentoGeradoRepositoryJpa extends JpaRepository<AgendamentoGerado, UUID> {

    List<AgendamentoGerado> findByFilter(UUID profissional, LocalDate data, UUID paciente, Boolean status);
}