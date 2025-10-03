package br.org.apae.api.controllers.HealthProfessional;

import br.org.apae.api.professional.facade.IHealthProfessionalFacade;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/professionals")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthProfessionalController {

    // Depende da interface
    private final IHealthProfessionalFacade service;

    @Autowired
    public HealthProfessionalController(IHealthProfessionalFacade service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HealthProfessionalResponseDTO> create(@RequestBody @Valid HealthProfessionalCreateDTO dto) {
        return ResponseEntity.ok(this.service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<HealthProfessionalResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthProfessionalResponseDTO> findById(@PathVariable UUID id) {
        HealthProfessionalResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthProfessionalResponseDTO> update(
            @PathVariable UUID id,
            @RequestBody @Valid HealthProfessionalUpdateDTO dto) {
        return ResponseEntity.ok(this.service.update(id, dto));
    }
}