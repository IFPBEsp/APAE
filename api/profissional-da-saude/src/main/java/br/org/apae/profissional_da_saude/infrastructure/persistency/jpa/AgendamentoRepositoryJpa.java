package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface AgendamentoRepositoryJpa extends JpaRepository<AgendamentoEntity, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE AgendamentoEntity a SET a.ativo = :ativo WHERE a.id = :id")
    void updateStatus(@Param("id") UUID id, @Param("ativo") Boolean ativo);
}
