package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import br.org.apae.profissional_da_saude.domain.model.AreaSaude;
import br.org.apae.profissional_da_saude.domain.repository.AreaSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.AreaSaudeRepositoryJpa;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AreaSaudeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AreaSaudeMapper.toEntity;
import static br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AreaSaudeMapper.toModel;
@Repository
public class AreaSaudeRepositoryImpl implements AreaSaudeRepository {

    private final AreaSaudeRepositoryJpa repositoryJpa;

    public AreaSaudeRepositoryImpl(AreaSaudeRepositoryJpa repositoryJpa) {
        this.repositoryJpa = repositoryJpa;
    }

    @Override
    public AreaSaude save(AreaSaude areaSaude) {
        return toModel(this.repositoryJpa.save(
                toEntity(areaSaude))
        );
    }

    @Override
    public Page<AreaSaude> findAll(Pageable pageable) {
        return this.repositoryJpa.findAll(pageable)
                .map(AreaSaudeMapper::toModel);
    }

    @Override
    public Optional<AreaSaude> findById(Integer id) {
        return this.repositoryJpa.findById(id)
                .map(AreaSaudeMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
         this.repositoryJpa.deleteById(id);
    }

    @Override
    public boolean existsByArea(String area) {
        return this.repositoryJpa.existsByArea(area);
    }
}
