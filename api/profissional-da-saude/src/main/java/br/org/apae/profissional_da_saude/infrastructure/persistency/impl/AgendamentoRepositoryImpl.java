package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.domain.repository.AgendamentoRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.AgendamentoRepositoryJpa;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AgendamentoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AgendamentoRepositoryImpl implements AgendamentoRepository {

    private final AgendamentoRepositoryJpa repository;

    public AgendamentoRepositoryImpl(AgendamentoRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public Agendamento save(Agendamento agendamento) {
        AgendamentoEntity entity = AgendamentoMapper.toEntity(agendamento);
        return AgendamentoMapper.toModel(this.repository.save(entity));
    }

    @Override
    public Page<Agendamento> findAll(Pageable pageable) {
        return this.repository.findAll(pageable).map(AgendamentoMapper::toModel);
    }

    @Override
    public Optional<Agendamento> findById(UUID id) {
        return this.repository.findById(id)
                .map(AgendamentoMapper::toModel);
    }

    @Override
    public Agendamento update(Agendamento agendamento) {
        AgendamentoEntity entity = AgendamentoMapper.toEntity(agendamento);
        if (agendamento.getId() != null) {
            repository.findById(agendamento.getId()).ifPresent(existingEntity -> {
                entity.setDataCriacao(existingEntity.getDataCriacao());
            });
        }
        return AgendamentoMapper.toModel(this.repository.save(entity));
    }

    @Override
    public void deleteById(UUID id) {
        this.repository.deleteById(id);
    }

     @Override
    public void updateStatus(UUID id, Boolean ativo) {
        this.repository.updateStatus(id, ativo);
    }
}
