package br.org.apae.profissional_da_saude.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.profissional_da_saude.api.dto.FaltaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.FaltaResponseDTO;
import br.org.apae.profissional_da_saude.application.service.FaltaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/faltas")
public class FaltaController {

    private final FaltaService service;

    public FaltaController(FaltaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FaltaResponseDTO> create(@RequestBody @Valid FaltaCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<FaltaResponseDTO>> findByAll(
            @RequestParam(name = "fkProfissional", required = false) UUID fkProfissional,
            @RequestParam(name = "fkAtendimento", required = false) UUID fkAtendimento,
            Pageable pageable) {

        return ResponseEntity.ok(this.service.findWithFilters(fkProfissional, fkAtendimento, pageable));
    }

}
