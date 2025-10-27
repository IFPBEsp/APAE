package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.org.apae.profissional_da_saude.domain.model.Falta;
import br.org.apae.profissional_da_saude.domain.repository.FaltaRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.FaltaEntity;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.FaltaRepositoryJpa;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.FaltaMapper;

@Repository
public class FaltaRepositoryImpl implements FaltaRepository {

    private final FaltaRepositoryJpa repository;

    public FaltaRepositoryImpl(FaltaRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public Falta save(Falta falta) {
        FaltaEntity entity = FaltaMapper.toEntity(falta);
        return FaltaMapper.toModel(this.repository.save(entity));
    }

    @Override
    public Page<Falta> findWithFilters(UUID fkProfissional, UUID fkAtendimento, Pageable pageable) {
        return this.repository.findWithFilters(fkProfissional, fkAtendimento, pageable)
                .map(FaltaMapper::toModel);
    }

}
