package br.org.apae.api.professional.interfaces.controllers;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/availabilities")
public interface AvailabilityController {

    @Operation(summary = "Criar nova disponibilidade", description = "Cria uma nova disponibilidade para um profissional de saúde.", responses = {
            @ApiResponse(responseCode = "201", description = "Disponibilidade criada com sucesso", content = @Content(schema = @Schema(implementation = AvailabilityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos obrigatórios vazios", content = @Content),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito: Já existe disponibilidade para este profissional, dia e turno", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping("/professional/{professionalId}")
    ResponseEntity<AvailabilityResponseDTO> createAvailability(
            @PathVariable UUID professionalId,
            @Valid @RequestBody CreateAvailabilityDTO dto);

    @Operation(summary = "Listar todas as disponibilidades", description = "Retorna uma lista de todas as disponibilidades registradas.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<AvailabilityResponseDTO>> getAllAvailabilities();

    @Operation(summary = "Buscar disponibilidade por ID", description = "Obtém os dados de uma disponibilidade específica através do seu identificador (UUID).", responses = {
            @ApiResponse(responseCode = "200", description = "Disponibilidade encontrada", content = @Content(schema = @Schema(implementation = AvailabilityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Disponibilidade não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<AvailabilityResponseDTO> getAvailabilityById(@PathVariable UUID id);

    @Operation(summary = "Listar disponibilidades por profissional", description = "Retorna todas as disponibilidades de um profissional específico.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/professional/{professionalId}")
    ResponseEntity<List<AvailabilityResponseDTO>> getAvailabilitiesByProfessional(@PathVariable UUID professionalId);

    @Operation(summary = "Atualizar disponibilidade", description = "Atualiza os horários ou status de uma disponibilidade existente.", responses = {
            @ApiResponse(responseCode = "200", description = "Disponibilidade atualizada com sucesso", content = @Content(schema = @Schema(implementation = AvailabilityResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Disponibilidade não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito: Já existe disponibilidade para este profissional, dia e turno", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<AvailabilityResponseDTO> updateAvailability(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAvailabilityDTO dto);

    @Operation(summary = "Excluir disponibilidade", description = "Remove uma disponibilidade existente pelo seu identificador (UUID).", responses = {
            @ApiResponse(responseCode = "204", description = "Disponibilidade excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Disponibilidade não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteAvailability(@PathVariable UUID id);
}
