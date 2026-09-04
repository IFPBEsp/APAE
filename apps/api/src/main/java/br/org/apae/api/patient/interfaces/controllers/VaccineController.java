package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

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

        @Operation(summary = "Cadastrar vacina", description = "Cria uma nova vacina. Falha se o nome já existir.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Vacina criada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "409", description = "Já existe uma vacina com este nome")
        })
        @PostMapping
        ResponseEntity<VaccineResponseDTO> createVaccine(@RequestBody CreateVaccineDTO dto);

        @Operation(summary = "Atualizar vacina", description = "Atualiza o nome de uma vacina existente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vacina atualizada com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Vacina não encontrada"),
                        @ApiResponse(responseCode = "409", description = "Nome já em uso por outra vacina")
        })
        @PutMapping("/{id}")
        ResponseEntity<VaccineResponseDTO> updateVaccine(@PathVariable UUID id, @RequestBody UpdateVaccineDTO dto);

        @Operation(summary = "Excluir vacina", description = "Remove uma vacina. Falha se estiver vinculada a um paciente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Vacina excluída com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Vacina não encontrada"),
                        @ApiResponse(responseCode = "409", description = "Vacina vinculada a paciente")
        })
        @DeleteMapping("/{id}")
        ResponseEntity<Void> deleteVaccine(@PathVariable UUID id);
}
