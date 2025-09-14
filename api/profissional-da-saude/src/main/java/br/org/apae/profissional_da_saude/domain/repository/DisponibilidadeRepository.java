package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<DisponibilidadeEntity, UUID> {

    // Método para deletar disponibilidades por profissional
    @Modifying
    @Query("DELETE FROM DisponibilidadeEntity d WHERE d.fkProfissionalId = :profissionalId")
    void deleteByProfissionalId(@Param("profissionalId") UUID profissionalId);

    // Método para buscar disponibilidades por profissional
    @Query("SELECT d FROM DisponibilidadeEntity d WHERE d.fkProfissionalId = :profissionalId")
    List<DisponibilidadeEntity> findByFkProfissionalId(@Param("profissionalId") UUID profissionalId);

    // Alternativa usando naming convention (Spring Data JPA criará automaticamente)
    // List<DisponibilidadeEntity> findByFkProfissionalId(UUID fkProfissionalId);

    // Método para verificar se já existe uma combinação dia/turno para um profissional
    @Query("SELECT COUNT(d) > 0 FROM DisponibilidadeEntity d WHERE d.fkProfissionalId = :profissionalId AND d.diaSemana = :dia AND d.turno = :turno")
    boolean existsByProfissionalIdAndDiaAndTurno(
            @Param("profissionalId") UUID profissionalId,
            @Param("dia") br.org.apae.profissional_da_saude.domain.model.enums.DiaSemana dia,
            @Param("turno") br.org.apae.profissional_da_saude.domain.model.enums.Turno turno
    );
}