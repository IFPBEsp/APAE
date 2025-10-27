package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoGeradoRepositoryJpa extends JpaRepository<AgendamentoGerado, UUID> {

    List<AgendamentoGerado> findAll();
    List<AgendamentoGerado> findByFilter(UUID profissional, LocalDate data, UUID paciente, Boolean status);
}