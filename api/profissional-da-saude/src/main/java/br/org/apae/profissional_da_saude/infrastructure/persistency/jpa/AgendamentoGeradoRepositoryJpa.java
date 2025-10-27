package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;

public interface AgendamentoGeradoRepositoryJpa extends JpaRepository<AgendamentoGerado, UUID> {

    @Query(value = "SELECT * FROM agendamentos_gerados", nativeQuery = true)
    List<AgendamentoGerado> findAll();

    @Query(value = "SELECT * FROM view_agendamentos_gerados " +
                   "WHERE (:profissional IS NULL OR fk_profissional = :profissional) " +
                   "AND (:data IS NULL OR data_inicial = :data) " +
                   "AND (:paciente IS NULL OR fk_cadastro_anual = :paciente) " +
                   "AND (:status IS NULL OR ativo = :status)", nativeQuery = true)
    List<AgendamentoGerado> findByFilter(@Param("profissional") UUID profissional,
                                         @Param("data") LocalDate data,
                                         @Param("paciente") UUID paciente,
                                         @Param("status") Boolean status);
}
