package com.apae.profissional_de_saude.api.controller;

import com.apae.profissional_de_saude.application.service.ProfissionalDeSaudeService;
import com.apae.profissional_de_saude.domain.model.ProfissionalDeSaude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profissionais-saude")
public class ProfissionalDeSaudeController {
    private final ProfissionalDeSaudeService service;

    public ProfissionalDeSaudeController(ProfissionalDeSaudeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalDeSaude>> listar() {
        List<ProfissionalDeSaude> profissionalDeSaudes = service.listarTodos();

        return ResponseEntity.status(HttpStatus.OK).body(profissionalDeSaudes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDeSaude> buscar(@PathVariable Long id) {
        ProfissionalDeSaude profissionalDeSaude = service.buscarPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body(profissionalDeSaude);
    }
}
