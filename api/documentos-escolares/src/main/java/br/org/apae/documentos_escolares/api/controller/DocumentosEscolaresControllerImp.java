package br.org.apae.documentos_escolares.api.controller;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.api.dto.response.VisualizacaoDocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.application.service.DocumentosEscolaresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documentos-escolares")
public class DocumentosEscolaresControllerImp implements DocumentosEscolaresController {

    private final DocumentosEscolaresService documentosEscolaresService;

    @Override
    public ResponseEntity<Void> anexarDocumentoEscolar(DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo) {
        documentosEscolaresService.salvarArquivo(dto, arquivo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
        return null;
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
