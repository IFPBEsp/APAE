package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.org.apae.profissional_da_saude.infrastructure.entity.FaltaEntity;

@Repository
public interface FaltaRepositoryJpa extends JpaRepository<FaltaEntity, UUID> {

        @Query("SELECT f FROM FaltaEntity f WHERE " +
                        "(:fkProfissional IS NULL OR f.fkProfissional = :fkProfissional) AND " +
                        "(:fkAtendimento IS NULL OR f.fkAtendimento = :fkAtendimento)")
        Page<FaltaEntity> findWithFilters(
                        @Param("fkProfissional") UUID fkProfissional,
                        @Param("fkAtendimento") UUID fkAtendimento,
                        Pageable pageable);
}
