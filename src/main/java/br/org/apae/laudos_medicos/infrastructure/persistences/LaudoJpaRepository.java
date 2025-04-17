package br.org.apae.laudos_medicos.infrastructure.persistences;

import br.org.apae.laudos_medicos.domain.entities.Laudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaudoJpaRepository extends JpaRepository<Laudo, Long> {
    @Query("SELECT l FROM Laudo l WHERE l.idPaciente = :idPaciente")
    List<Laudo> findByPacienteId(Long idPaciente);
}
