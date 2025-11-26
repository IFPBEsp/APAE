package br.org.apae.api.servicearea.interfaces.controllers;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/service-areas")
public interface ServiceAreaController {

    @Operation(summary = "Cadastrar área de atendimento", description = "Cria uma nova área de atendimento no sistema.", responses = {
            @ApiResponse(responseCode = "201", description = "Área de atendimento criada com sucesso", content = @Content(schema = @Schema(implementation = ServiceAreaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Área de atendimento já existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<ServiceAreaResponseDTO> createServiceArea(CreateServiceAreaDTO dto);

    @Operation(summary = "Listar áreas de atendimento", description = "Retorna uma lista paginada de todas as áreas de atendimento cadastradas.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso", content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<ServiceAreaResponseDTO>> getAllServiceAreas(Pageable pageable);

    @Operation(summary = "Excluir área de atendimento", description = "Remove uma área de atendimento pelo seu identificador (UUID).", responses = {
            @ApiResponse(responseCode = "204", description = "Área de atendimento excluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Área de atendimento não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteServiceArea(@PathVariable UUID id);

    @Operation(summary = "Buscar área de atendimento por ID", description = "Obtém os dados de uma área de atendimento específica através do seu identificador (UUID).", responses = {
            @ApiResponse(responseCode = "200", description = "Área de atendimento encontrada", content = @Content(schema = @Schema(implementation = ServiceAreaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Área de atendimento não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<ServiceAreaResponseDTO> findByIdServiceArea(@PathVariable UUID id);

    @Operation(summary = "Atualizar área de atendimento", description = "Atualiza as informações de uma área de atendimento existente.", responses = {
            @ApiResponse(responseCode = "200", description = "Área de atendimento atualizada com sucesso", content = @Content(schema = @Schema(implementation = ServiceAreaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Área de atendimento não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Área de atendimento já existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<ServiceAreaResponseDTO> updateServiceArea(@PathVariable UUID id, UpdateServiceAreaDTO dto);
}

