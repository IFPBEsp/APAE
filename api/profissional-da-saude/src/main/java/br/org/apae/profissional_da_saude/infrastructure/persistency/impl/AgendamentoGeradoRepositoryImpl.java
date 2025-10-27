package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;
import br.org.apae.profissional_da_saude.domain.repository.AgendamentoGeradoRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.AgendamentoGeradoRepositoryJpa;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AgendamentoGeradoMapper;


@Repository
public class AgendamentoGeradoRepositoryImpl implements AgendamentoGeradoRepository{

    private final AgendamentoGeradoRepositoryJpa repository;

    public AgendamentoGeradoRepositoryImpl(AgendamentoGeradoRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    List<AgendamentoGerado> findAll(){
        return this.repository.findAll().stream()
                .map(AgendamentoGeradoMapper::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    List<AgendamentoGerado> findByFilter(UUID profissional, LocalDate data, UUID paciente, Boolean status){
        return this.repository.findByFilter(profissional, data, paciente, status).stream()
                .map(AgendamentoGeradoMapper::toModel)
                .collect(Collectors.toList());
    }
}