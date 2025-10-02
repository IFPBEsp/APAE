package br.org.apae.api.controllers.HealthProfessional;

import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/profissionais")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthProfessionalController {

    private final br.org.apae.api.professional.services.HealthProfessionalService service;

    @Autowired
    public HealthProfessionalController(br.org.apae.api.professional.services.HealthProfessionalService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<br.org.apae.api.professional.dto.HealthProfessionalResponseDTO> create(@RequestBody @Valid HealthProfessionalCreateDTO dto) {
        return ResponseEntity.ok(this.service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<br.org.apae.api.professional.dto.HealthProfessionalResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<br.org.apae.api.professional.dto.HealthProfessionalResponseDTO> findById(@PathVariable UUID id) {
        br.org.apae.api.professional.dto.HealthProfessionalResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<br.org.apae.api.professional.dto.HealthProfessionalResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO dto) {
        return ResponseEntity.ok(this.service.update(id, dto));
    }
}