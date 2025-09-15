package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;
import br.org.apae.profissional_da_saude.domain.model.enums.DiaSemana;
import br.org.apae.profissional_da_saude.domain.model.enums.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisponibilidadeRepository extends JpaRepository<DisponibilidadeEntity, UUID> {

    // Deletar disponibilidades por profissional
    @Modifying
    @Query("DELETE FROM DisponibilidadeEntity d WHERE d.profissional.id = :profissionalId")
    void deleteByProfissionalId(@Param("profissionalId") UUID profissionalId);

    // Buscar disponibilidades por profissional
    @Query("SELECT d FROM DisponibilidadeEntity d WHERE d.profissional.id = :profissionalId")
    List<DisponibilidadeEntity> findByProfissionalId(@Param("profissionalId") UUID profissionalId);

    // Verificar se já existe uma combinação dia/turno para um profissional
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM DisponibilidadeEntity d " +
            "WHERE d.profissional.id = :profissionalId " +
            "AND d.diaSemana = :dia " +
            "AND d.turno = :turno")
    boolean existsByProfissionalIdAndDiaAndTurno(
            @Param("profissionalId") UUID profissionalId,
            @Param("dia") DiaSemana dia,
            @Param("turno") Turno turno);
}
