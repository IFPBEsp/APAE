package br.org.apae.laudos_medicos.domain.repositories;


import java.util.List;

import org.springframework.stereotype.Repository;

import br.org.apae.laudos_medicos.domain.entities.Laudo;

@Repository
public interface LaudoRepository{
    Laudo save(Laudo laudo);
    Laudo findById(Long id);
    List<Laudo> findAll();
    void delete(Long id);
    List<Laudo> findLaudosByPacientId(Long idPaciente);

}
