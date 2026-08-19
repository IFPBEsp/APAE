package br.org.apae.api.patient.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/vaccines")
@Tag(name = "Vaccines", description = "Endpoints para consulta de vacinas")
public interface VaccineController {

        @Operation(summary = "Buscar vacina por ID", description = "Retorna os dados de uma vacina específica pelo seu ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vacina encontrada com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
        })
        @GetMapping("/{id}")
        ResponseEntity<VaccineResponseDTO> findById(@PathVariable UUID id);

        @Operation(summary = "Listar todas as vacinas", description = "Retorna a lista completa de vacinas cadastradas no sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de vacinas retornada com sucesso")
        })
        @GetMapping
        ResponseEntity<List<VaccineResponseDTO>> findAll();

        @Operation(summary = "Buscar vacina por nome", description = "Retorna os dados de uma vacina específica pelo seu nome único.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vacina encontrada com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
        })
        @GetMapping("/search/by-name")
        ResponseEntity<VaccineResponseDTO> findByName(@RequestParam String name);
        
        @PostMapping
        ResponseEntity<VaccineResponseDTO> createVaccine(@jakarta.validation.Valid @RequestBody CreateVaccineDTO dto);

        @PutMapping("/{id}")
        ResponseEntity<VaccineResponseDTO> updateVaccine(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody UpdateVaccineDTO dto);

        @DeleteMapping("/{id}")
        ResponseEntity<Void> deleteVaccine(@PathVariable UUID id);
}
