package br.org.apae.profissional_da_saude.domain.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.profissional_da_saude.domain.model.Falta;

public interface FaltaRepository {

    Falta save(Falta falta);

    Page<Falta> findWithFilters(UUID fkProfissional, UUID fkAtendimento, Pageable pageable);
}
