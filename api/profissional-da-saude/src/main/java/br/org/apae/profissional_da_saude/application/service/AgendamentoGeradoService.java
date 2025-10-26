package br.org.apae.profissional_da_saude.application.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;
import br.org.apae.profissional_da_saude.domain.repository.AgendamentoGeradoRepository;

@Service
public class AgendamentoGeradoService {
    
    private final AgendamentoGeradoRepository repository;

    public AgendamentoGeradoService(AgendamentoGeradoRepository repository) {
        this.repository = repository;
    }
    
    public List<AgendamentoGerado> getAll() {
        return repository.findAll();
    }
    
    public List<AgendamentoGerado> getFiltered(UUID idProfissional, LocalDate data, UUID idPaciente, Boolean status) {
        return repository.findByFilter(idProfissional, data, idPaciente, status);
    }
}