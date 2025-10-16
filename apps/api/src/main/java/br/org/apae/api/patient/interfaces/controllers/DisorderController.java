package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/transtornos")
public interface DisorderController {

    @Operation(summary = "Cadastrar ou Retornar Transtorno", description = "Busca um transtorno por nome. Se existir, retorna o existente (200 OK). Se não, cria um novo (201 Created).", responses = {
            @ApiResponse(responseCode = "201", description = "Transtorno criado com sucesso", content = @Content(schema = @Schema(implementation = DisorderResponseDTO.class))),
            @ApiResponse(responseCode = "200", description = "Transtorno já existia e foi retornado", content = @Content(schema = @Schema(implementation = DisorderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    ResponseEntity<DisorderResponseDTO> createDisorder(@RequestBody CreateDisorderDTO dto);

    @Operation(summary = "Listar todos os Transtornos", description = "Retorna uma lista completa de todos os transtornos cadastrados (sem paginação).", responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso", content = @Content(schema = @Schema(implementation = List.class)))
    })
    @GetMapping
    ResponseEntity<List<DisorderResponseDTO>> getAllDisorders();

    @Operation(summary = "Excluir Transtorno", description = "Remove um transtorno pelo seu identificador (UUID).", responses = {
            @ApiResponse(responseCode = "204", description = "Transtorno excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transtorno não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteDisorder(@PathVariable UUID id);

    @Operation(summary = "Buscar Transtorno por ID", description = "Obtém os dados de um transtorno específico através do seu identificador (UUID).", responses = {
            @ApiResponse(responseCode = "200", description = "Transtorno encontrado", content = @Content(schema = @Schema(implementation = DisorderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transtorno não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<DisorderResponseDTO> findByIdDisorder(@PathVariable UUID id);

    @Operation(summary = "Atualizar Transtorno", description = "Atualiza o nome de um transtorno existente.", responses = {
            @ApiResponse(responseCode = "200", description = "Transtorno atualizado com sucesso", content = @Content(schema = @Schema(implementation = DisorderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transtorno não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito (Nome já existe)", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<DisorderResponseDTO> updateDisorder(@PathVariable UUID id, @RequestBody UpdateDisorderDTO dto);
}