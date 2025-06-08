package br.org.apae.documentos_escolares.api.controller;

import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface DocumentosEscolaresController {
    @Operation(
            summary = "Anexa o documento no bucket/documentos-escolar/ano",
            description = "Recebe por parâmetro o UUID do paciente, ano e o documento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento anexado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao anexar documento"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado"),
            @ApiResponse(responseCode = "500", description = "Documento não anexado por erro do minIO")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> anexarDocumentoEscolar(
            @RequestParam UUID paciente,
            @RequestParam Integer ano,
            @RequestPart("arquivo") MultipartFile arquivo
    );

    @Operation(summary = "Retorna listagem de todos ou por ano documentos de um paciênte", description = "Recebe por parâmetro o UUID do paciênte e um parâmetro opcional \"ano\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documentos listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @GetMapping("/{pacienteId}/filtrar")
    ResponseEntity<DocumentoEscolarResponseDTO> listarDocumentosEscolares(
            @PathVariable("pacienteId") UUID pacienteId,
            @RequestParam(value = "ano", required = false) Integer ano
    );

    @Operation(summary = "Retorna o histórico de documentos de um paciênte", description = "Recebe por parâmetro o UUID do paciênte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico dos documentos listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @GetMapping("/{pacienteId}/historico")
    ResponseEntity<DocumentoEscolarResponseDTO> historicoDocumentosEscolares(
            @PathVariable("pacienteId") UUID pacienteId
    );

    @Operation(summary = "Retorna o documento de um paciênte para visualizar ou fazer download", description = "Recebe por parâmetro o UUID do paciênte e o nome do documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado ou documento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @GetMapping("/{pacienteId}/visualizar")
    ResponseEntity<byte[]> visualizarDocumentoEscolar(
            @PathVariable("pacienteId") UUID pacienteId,
            @RequestParam String nomeArquivo
    );

    @Operation(summary = "Atualiza documento de um paciênte", description = "Recebe por parâmetro o UUID do paciênte, ano, nome do documento e o documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado ou documento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> atualizarDocumentoEscolar( @RequestParam UUID paciente,
                                                    @RequestParam Integer ano,
                                                    @RequestParam String documentoNome,
                                                    @RequestPart("arquivo") MultipartFile arquivo);

    @Operation(summary = "Remove documento de um paciênte", description = "Recebe por parâmetro o UUID do paciênte e o nome do documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado ou documento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @DeleteMapping("/{pacienteId}")
    ResponseEntity<Void> removerDocumentoEscolar(
            @PathVariable("pacienteId") UUID pacienteId,
            @RequestParam String nomeArquivo
    );
}
