package com.apae.profissional_de_saude.domain.repository;

import com.apae.profissional_de_saude.domain.model.ProfissionalDeSaude;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfissionalDeSaudeRepository {
    List<ProfissionalDeSaude> findAll();
    Optional<ProfissionalDeSaude> findById(Long id);
    void save(ProfissionalDeSaude profissionalDeSaude);
    void update(ProfissionalDeSaude profissionalDeSaude);
    void delete(Long id);
}
