package br.org.apae.profissional_da_saude.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.profissional_da_saude.api.dto.FaltaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.FaltaResponseDTO;
import br.org.apae.profissional_da_saude.application.service.FaltaService;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Falta", description = "Endpoints para gerenciamento de Faltas de Pacientes")
@RestController
@RequestMapping("/faltas")
public class FaltaController {

    private final FaltaService service;

    public FaltaController(FaltaService service) {
        this.service = service;
    }

    @Operation(summary = "Registra uma nova falta", description = "Cria um novo registro de falta no sistema, seja de um profissional ou em um atendimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Falta registrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaltaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @io.swagger.v3.oas.annotations.media.Content),
    })
    @PostMapping
    public ResponseEntity<FaltaResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para criação da falta", required = true) @RequestBody @Valid FaltaCreateDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Lista faltas com filtros opcionais", description = "Retorna uma lista paginada de faltas, permitindo filtrar por profissional ou atendimento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de faltas retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetro de filtro ou paginação inválido"),
    })

    @GetMapping
    public ResponseEntity<Page<FaltaResponseDTO>> findByAll(
            @Parameter(description = "ID do profissional para filtrar as faltas") @RequestParam(name = "fkProfissional", required = false) UUID fkProfissional,
            @Parameter(description = "ID do atendimento para filtrar as faltas (opcional)") @RequestParam(name = "fkAtendimento", required = false) UUID fkAtendimento,
            Pageable pageable) {

        return ResponseEntity.ok(this.service.findWithFilters(fkProfissional, fkAtendimento, pageable));
    }

}
