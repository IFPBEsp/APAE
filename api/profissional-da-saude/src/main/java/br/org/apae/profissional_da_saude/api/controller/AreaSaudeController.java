package br.org.apae.profissional_da_saude.api.controller;

import br.org.apae.profissional_da_saude.api.dto.AreaSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AreaSaudeResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.AreaSaudeUpdateDTO;
import br.org.apae.profissional_da_saude.application.service.AreaSaudeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/areaSaude")
@CrossOrigin(origins = "http://localhost:3000")
public class AreaSaudeController {

    private final AreaSaudeService service;

    public AreaSaudeController(AreaSaudeService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<AreaSaudeResponseDTO> create(@RequestBody @Valid AreaSaudeCreateDTO dto){
        return ResponseEntity.ok(this.service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<AreaSaudeResponseDTO>> findAll(Pageable pageable){
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AreaSaudeResponseDTO> findById(@PathVariable Integer id){
        return this.service.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AreaSaudeResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid AreaSaudeUpdateDTO dto){
        return ResponseEntity.ok(this.service.update(id, dto));
    }
}
