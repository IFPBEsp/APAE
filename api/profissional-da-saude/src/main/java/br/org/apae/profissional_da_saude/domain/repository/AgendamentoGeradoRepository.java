package br.org.apae.profissional_da_saude.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;


public interface AgendamentoGeradoRepository {

    List<AgendamentoGerado> findAll();

    List<AgendamentoGerado> findByFilter(UUID profissional, LocalDate data, UUID paciente, Boolean status);
    
}