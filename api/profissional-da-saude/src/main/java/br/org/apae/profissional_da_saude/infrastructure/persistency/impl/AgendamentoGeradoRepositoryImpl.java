package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;
import br.org.apae.profissional_da_saude.domain.repository.AgendamentoGeradoRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.AgendamentoGeradoRepositoryJpa;


@Repository
public class AgendamentoGeradoRepositoryImpl implements AgendamentoGeradoRepository{

    private final AgendamentoGeradoRepositoryJpa repository;

    public AgendamentoGeradoRepositoryImpl(AgendamentoGeradoRepositoryJpa repository) {
        this.repository = repository;
    }

    @Override
    public List<AgendamentoGerado> findAll(){
        return this.repository.findAll();
    }
    
    @Override
    public List<AgendamentoGerado> findByFilter(UUID profissional, LocalDate data, UUID paciente, Boolean status){
        return this.repository.findByFilter(profissional, data, paciente, status);
    }
}