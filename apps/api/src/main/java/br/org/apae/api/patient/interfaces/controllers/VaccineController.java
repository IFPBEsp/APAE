package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/vaccines")
@Tag(name = "Vaccines", description = "Endpoints para consulta e gerenciamento de vacinas")
public interface VaccineController {

    @Operation(
            summary = "Cadastrar Vacina",
            description = "Cria um novo registro de vacina no sistema. Falha se o nome já existir.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Vacina criada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = VaccineResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflito: vacina com este nome já existe.",
                            content = @Content
                    )
            }
    )
    @PostMapping
    ResponseEntity<VaccineResponseDTO> createVaccine(
            @Valid @RequestBody CreateVaccineDTO dto
    );

    @Operation(
            summary = "Listar todas as vacinas",
            description = "Retorna uma lista completa de todas as vacinas cadastradas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista obtida com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = List.class)
                            )
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<VaccineResponseDTO>> getAllVaccines();

    @Operation(
            summary = "Excluir Vacina",
            description = "Remove uma vacina pelo seu identificador (UUID).",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Vacina excluída com sucesso",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacina não encontrada",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Vacina vinculada a paciente",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVaccine(
            @PathVariable UUID id
    );

    @Operation(
            summary = "Buscar Vacina por ID",
            description = "Obtém os dados de uma vacina específica através do seu identificador (UUID).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vacina encontrada",
                            content = @Content(
                                    schema = @Schema(implementation = VaccineResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacina não encontrada",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<VaccineResponseDTO> findById(
            @PathVariable UUID id
    );

    @Operation(
            summary = "Buscar Vacina por nome",
            description = "Retorna os dados de uma vacina específica pelo seu nome único.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vacina encontrada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = VaccineResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacina não encontrada",
                            content = @Content
                    )
            }
    )
    @GetMapping("/search/by-name")
    ResponseEntity<VaccineResponseDTO> findByName(
            @RequestParam String name
    );

    @Operation(
            summary = "Atualizar Vacina",
            description = "Atualiza o nome de uma vacina existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Vacina atualizada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = VaccineResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Vacina não encontrada",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflito: nome da vacina já existe",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<VaccineResponseDTO> updateVaccine(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVaccineDTO dto
    );
}