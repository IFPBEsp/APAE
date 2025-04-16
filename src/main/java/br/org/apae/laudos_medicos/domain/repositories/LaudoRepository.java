package br.org.apae.laudos_medicos.domain.repositories;

import org.springframework.stereotype.Repository;

import br.org.apae.laudos_medicos.domain.entities.Laudo;

@Repository
public interface LaudoRepository extends GenericRepository<Laudo, Long>{

}
