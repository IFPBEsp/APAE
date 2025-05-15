package br.org.apae.documentos_escolares.api.controller;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.application.service.DocumentosEscolaresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documentos-escolares")
public class DocumentosEscolaresControllerImp implements DocumentosEscolaresController {

    private final DocumentosEscolaresService documentosEscolaresService;

    @Operation(summary = "Anexa o documento no bucket/documentos-escolar/ano", description = "Recebe por parâmetro o UUID do paciênte e o documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento anexado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao anexar documento"),
            @ApiResponse(responseCode = "500", description = "Documento não anexado por erro do minIO")
    })
    @Override
    public ResponseEntity<Void> anexarDocumentoEscolar(DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo) {
        documentosEscolaresService.salvarArquivo(dto, arquivo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Retorna listagem de todos ou por ano documentos de um paciênte", description = "Recebe por parâmetro o UUID do paciênte e um parâmetro opcional \"ano\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documentos listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciênte não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @Override
    public ResponseEntity<DocumentoEscolarResponseDTO> listarDocumentosEscolares(UUID pacienteId, Integer ano) {
        DocumentoEscolarResponseDTO responseDTO = documentosEscolaresService.listarDocumentosEscolares(pacienteId);
        if (ano != null) {
            responseDTO = documentosEscolaresService.listarDocumentosEscolaresAno(pacienteId, ano);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Override
    public ResponseEntity<DocumentoEscolarResponseDTO> historicoDocumentosEscolares(UUID pacienteId) {
        DocumentoEscolarResponseDTO responseDTO = documentosEscolaresService.historicoDocumentosEscolares(pacienteId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Override
    public ResponseEntity<byte[]> visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        byte[] response = documentosEscolaresService.visualizarDocumentoEscolar(pacienteId, nomeArquivo);

        MediaType contentType = getContentType(nomeArquivo);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nomeArquivo + "\"")
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        documentosEscolaresService.deletarDocumentoEscolar(pacienteId, nomeArquivo);

        return ResponseEntity.noContent().build();
    }

    private MediaType getContentType(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF;
        } else if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
