package br.org.apae.profissional_da_saude.api.controller;


import br.org.apae.profissional_da_saude.api.dto.PacienteResposeDto;
import br.org.apae.profissional_da_saude.application.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResposeDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResposeDto>> findByAll(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }


}
