package com.apae.profissional_de_saude.application.service;

import com.apae.profissional_de_saude.domain.model.ProfissionalDeSaude;
import com.apae.profissional_de_saude.domain.repository.ProfissionalDeSaudeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalDeSaudeService {
    private final ProfissionalDeSaudeRepository repository;

    public ProfissionalDeSaudeService(ProfissionalDeSaudeRepository repository) {
        this.repository = repository;
    }

    public List<ProfissionalDeSaude> listarTodos() {
        return repository.findAll();
    }

    public Optional<ProfissionalDeSaude> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void salvar(ProfissionalDeSaude profissionalDeSaude) {
        repository.save(profissionalDeSaude);
    }

    public void atualizar(ProfissionalDeSaude profissionalDeSaude) {
        repository.update(profissionalDeSaude);
    }

    public void remover(Long id) {
        repository.delete(id);
    }
}
