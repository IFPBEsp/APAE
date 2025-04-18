package br.org.apae.api_crud_medicos.controller;
import br.org.apae.api_crud_medicos.controller.dto.request.MedicoRequestDTO;
import br.org.apae.api_crud_medicos.controller.dto.response.MedicoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/medicos")
public interface MedicoController {

    @GetMapping
    ResponseEntity<List<MedicoResponseDTO>> getAll();

    @GetMapping("/{id}")
    ResponseEntity<MedicoResponseDTO> getById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<MedicoResponseDTO> create(@RequestBody MedicoRequestDTO request);

    @PutMapping("/{id}")
    ResponseEntity<MedicoResponseDTO> update(
            @PathVariable Long id,
            @RequestBody MedicoRequestDTO request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}