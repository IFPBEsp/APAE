package br.org.apae.profissional_da_saude.domain.repository;

import java.nio.file.OpenOption;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;

public interface ProfissionalSaudeRepository {
  ProfissionalSaude save(ProfissionalSaude profissionalSaude);

  Page<ProfissionalSaude> findAll(Pageable pageable);

  Page<String> findAllAreas(Pageable pageable);
  
  Optional<ProfissionalSaude> findById(UUID id);
  
  //TODO
  ProfissionalSaude update(ProfissionalSaude profissionalSaude);
  
  //TODO
  void deleteById(UUID id);
}
