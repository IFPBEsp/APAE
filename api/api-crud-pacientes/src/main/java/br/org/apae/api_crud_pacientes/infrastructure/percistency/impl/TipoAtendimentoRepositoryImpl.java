package br.org.apae.api_crud_pacientes.infrastructure.percistency.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;
import br.org.apae.api_crud_pacientes.domain.repository.TipoAtendimentoRepository;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.TipoAtendimentoMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.TipoAtendimentoRepositoryJpa;

@Repository
public class TipoAtendimentoRepositoryImpl implements TipoAtendimentoRepository {

    private final TipoAtendimentoRepositoryJpa repository;
    private final TipoAtendimentoMapper mapper;

    @Autowired
    public TipoAtendimentoRepositoryImpl(TipoAtendimentoRepositoryJpa repository, TipoAtendimentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TipoAtendimento save(TipoAtendimento tipoAtendimento) {
        var entity = mapper.toEntityFromDomain(tipoAtendimento);
        var saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public TipoAtendimento findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Page<TipoAtendimento> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public TipoAtendimento update(TipoAtendimento tipoAtendimento) {
        return save(tipoAtendimento);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<TipoAtendimento> findByDescricaoIgnoreCase(String descricao, Pageable pageable) {
        return repository.findByDescricaoContainingIgnoreCase(descricao, pageable)
                .map(mapper::toDomain);
    }
    
}