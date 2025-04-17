package br.org.apae.laudos_medicos.domain.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.org.apae.laudos_medicos.domain.entities.Laudo;

@Repository
public interface LaudoRepository{
    Laudo save(Laudo laudo);
    Optional<Laudo> findById(Long id);
    List<Laudo> findAll();
    void delete(Laudo laudo);
    List<Laudo> findLaudosByPacientId(Long idPaciente);
}
