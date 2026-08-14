package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import jakarta.validation.Valid;

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

        @Operation(summary = "Criar nova vacina", description = "Cadastra uma nova vacina no sistema.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Vacina criada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "409", description = "Já existe uma vacina com este nome")
        })
        @PostMapping
        ResponseEntity<VaccineResponseDTO> create(@Valid @RequestBody CreateVaccineDTO dto);

        @Operation(summary = "Editar vacina", description = "Atualiza o nome de uma vacina existente pelo seu ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Vacina atualizada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "404", description = "Vacina não encontrada"),
                        @ApiResponse(responseCode = "409", description = "Já existe outra vacina com este nome")
        })
        @PutMapping("/{id}")
        ResponseEntity<VaccineResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody UpdateVaccineDTO dto);

        @Operation(summary = "Excluir vacina", description = "Remove uma vacina existente pelo seu ID. Não permite excluir se estiver vinculada a um paciente.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Vacina excluída com sucesso (sem conteúdo)"),
                        @ApiResponse(responseCode = "404", description = "Vacina não encontrada"),
                        @ApiResponse(responseCode = "409", description = "Vacina vinculada a pacientes")
        })
        @DeleteMapping("/{id}")
        ResponseEntity<Void> delete(@PathVariable UUID id);
}
