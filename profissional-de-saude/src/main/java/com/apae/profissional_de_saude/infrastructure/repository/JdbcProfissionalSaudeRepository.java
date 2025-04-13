package com.apae.profissional_de_saude.infrastructure.repository;

import com.apae.profissional_de_saude.domain.model.ProfissionalDeSaude;
import com.apae.profissional_de_saude.domain.repository.ProfissionalDeSaudeRepository;

import java.util.List;
import java.util.Optional;

public class JdbcProfissionalSaudeRepository implements ProfissionalDeSaudeRepository {
    @Override
    public List<ProfissionalDeSaude> findAll() {
        // TODO -> implementar método
        return List.of();
    }

    @Override
    public Optional<ProfissionalDeSaude> findById(Long id) {
        // TODO -> implementar método
        return Optional.empty();
    }

    @Override
    public void save(ProfissionalDeSaude profissionalDeSaude) {
        // TODO -> implementar método
    }

    @Override
    public void update(ProfissionalDeSaude profissionalDeSaude) {
        // TODO -> implementar método
    }

    @Override
    public void delete(Long id) {
        // TODO -> implementar método
    }

}
