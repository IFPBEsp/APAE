package br.org.apae.documentos_digitalizados.api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_digitalizados.api.dto.DocumentObjectRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.DocumentsResponseDTO;
import br.org.apae.documentos_digitalizados.domain.DocumentCategory;
import br.org.apae.documentos_digitalizados.domain.DocumentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RequestMapping("/api/documents")
@SecurityRequirement(name = "bearerAuth") 
public interface IDigitizedDocumentAPI {
     @Operation(
        summary = "Criar bucket de documentos para o paciente",
        responses = {
            @ApiResponse(responseCode = "201", description = "Bucket criado com sucesso")
        }
    )
    @PostMapping("/bucket/{patientId}")
    ResponseEntity<Void> createBucket(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId
    );

    @Operation(
        summary = "Remover bucket de documentos do paciente",
        responses = {
            @ApiResponse(responseCode = "204", description = "Bucket removido com sucesso")
        }
    )
    @DeleteMapping("/bucket/{patientId}")
    ResponseEntity<Void> deleteBucket(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId
    );

    @Operation(
        summary = "Fazer upload de documento",
        requestBody = @RequestBody(
            description = "Dados do documento e o arquivo",
            content = @Content(mediaType = "multipart/form-data")
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Upload realizado com sucesso")
        }
    )

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    ResponseEntity<Void> uploadDocument(
        @RequestPart("document") DocumentObjectRequestDTO dto,
        @RequestPart("file") MultipartFile file
    );

    @Operation(
        summary = "Listar documentos do paciente por ano e categoria",
        responses = {
            @ApiResponse(responseCode = "200", description = "Documentos encontrados")
        }
    )
    @GetMapping("/{patientId}")
    ResponseEntity<DocumentsResponseDTO> listDocuments(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId,
        @Parameter(
          description = "Categoria do documento",
          schema = @Schema(implementation = DocumentCategory.class)
      )
        @RequestParam String category,
        @RequestParam Integer year
    );

    @Operation(
        summary = "Listar documentos do paciente por tipo",
        responses = {
            @ApiResponse(responseCode = "200", description = "Documentos encontrados")
        }
    )
    @GetMapping("/{patientId}/type")
    ResponseEntity<DocumentsResponseDTO> listDocumentsByType(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId,
        @Parameter(
          description = "Categoria do documento",
          schema = @Schema(implementation = DocumentCategory.class)
      )
        @RequestParam String category,
        @RequestParam Integer year,
        @Parameter(
          description = "Tipo do documento (Precisa ser um dos tipos definidos de acordo com a categoria)",
          schema = @Schema(implementation = DocumentType.class)
        )
        @RequestParam String type
    );

    @Operation(
        summary = "Obter histórico de documentos do paciente por tipo",
        responses = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado")
        }
    )
    @GetMapping("/{patientId}/history")
    ResponseEntity<DocumentsResponseDTO> getDocumentHistory(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId,
        @Parameter(
          description = "Categoria do documento",
          schema = @Schema(implementation = DocumentCategory.class)
        )
        @RequestParam String category,
        @Parameter(
          description = "Tipo do documento (Precisa ser um dos tipos definidos de acordo com a categoria)",
          schema = @Schema(implementation = DocumentType.class)
        )
        @RequestParam String type
    );

    @Operation(
        summary = "Visualizar documento como imagem PNG",
        responses = {
            @ApiResponse(responseCode = "200", description = "Documento retornado como imagem")
        }
    )
    @GetMapping(value = "/{patientId}/view", produces = "image/png")
    ResponseEntity<byte[]> viewDocument(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId,
        @RequestParam String path
    );

    @Operation(
        summary = "Excluir documento específico",
        responses = {
            @ApiResponse(responseCode = "204", description = "Documento excluído com sucesso")
        }
    )
    @DeleteMapping("/{patientId}/delete")
    ResponseEntity<Void> deleteDocument(
        @Parameter(description = "ID do paciente") @PathVariable UUID patientId,
        @RequestParam String fileName
    );
}
