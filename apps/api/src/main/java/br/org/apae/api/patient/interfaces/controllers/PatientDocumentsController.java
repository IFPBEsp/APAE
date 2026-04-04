package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/patients/{id}/documents")
@Tag(name = "Patient Documents", description = "Endpoints para gerenciamento de documentos do paciente")
public interface PatientDocumentsController {

    @Operation(summary = "Listar documentos médicos")
    @GetMapping("/medicals")
    ResponseEntity<List<DocumentWithUrlResponseDTO>> findMedicalDocuments(
            @PathVariable UUID id,
            @RequestParam(required = false) Integer year);

    @Operation(summary = "Listar documentos pessoais")
    @GetMapping("/personals")
    ResponseEntity<List<DocumentWithUrlResponseDTO>> findPersonalDocuments(
            @PathVariable UUID id,
            @RequestParam(required = false) Integer year);

    @Operation(summary = "Listar documentos escolares")
    @GetMapping("/schools")
    ResponseEntity<List<DocumentWithUrlResponseDTO>> findSchoolDocuments(
            @PathVariable UUID id,
            @RequestParam(required = false) Integer year);

    @Operation(summary = "Fazer upload de um documento")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<DocumentDTO> uploadDocument(
            @PathVariable UUID id,
            @RequestPart("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam("type") String type,
            @RequestParam(required = false) Integer year
    );
}