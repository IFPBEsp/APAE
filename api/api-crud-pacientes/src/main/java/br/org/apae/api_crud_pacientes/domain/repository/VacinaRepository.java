package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.Vacina;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VacinaRepository {
  Vacina save(Vacina vacina);

  Vacina findById(UUID id);

  Page<Vacina> findAll(Pageable pageable);

  Page<Vacina> findByNomeIgnoreCase(String nome, Pageable pageable);

  Vacina update(Vacina vacina);

  void deleteById(UUID id);
}
