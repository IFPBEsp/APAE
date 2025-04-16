package br.org.apae.laudos_medicos.domain.repositories;

import br.org.apae.laudos_medicos.domain.entities.Laudo;

import java.util.List;

public interface LaudoRepository {
    Laudo save(Laudo laudo);
    Laudo findById(Long id);
    List<Laudo> findAll();
    void delete(Long id);
    List<Laudo> findLaudosByPacientId(Long idPaciente);
}
