package br.org.apae.documentos_digitalizados.api.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_digitalizados.api.dto.DocumentObjectRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.DocumentsResponseDTO;
import br.org.apae.documentos_digitalizados.application.DigitizedDocumentService;

@RestController
public class DigitizedDocumentController implements IDigitizedDocumentAPI{

  private final DigitizedDocumentService documentService;

  @Autowired
  public DigitizedDocumentController(DigitizedDocumentService documentService) {
    this.documentService = documentService;
  }

  @Override
  public ResponseEntity<Void> createBucket(@PathVariable UUID patientId) {
    documentService.createNewBucket(patientId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<Void> deleteBucket(@PathVariable UUID patientId) {
    documentService.removeBucket(patientId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> uploadDocument(
    @RequestPart("document") DocumentObjectRequestDTO dto,
    @RequestPart("file") MultipartFile file
  ) {
    System.out.println("DTO recebido: " + dto);
    System.out.println("PatientId: " + dto.patientId());
    System.out.println("Year: " + dto.year());
    System.out.println("Category: " + dto.documentCategory());
    System.out.println("Type: " + dto.documentType());
    System.out.println("Arquivo: " + file.getOriginalFilename());
    documentService.saveFile(dto, file);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<DocumentsResponseDTO> listDocuments(
    @PathVariable UUID patientId,
    @RequestParam String category,
    @RequestParam Integer year
  ) {
    DocumentsResponseDTO response = documentService.listDocument(patientId, category, year);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<DocumentsResponseDTO> listDocumentsByType(
    @PathVariable UUID patientId,
    @RequestParam String category,
    @RequestParam Integer year,
    @RequestParam String type
  ) {
    DocumentsResponseDTO response = documentService.listDocumentByType(patientId, year, category, type);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<DocumentsResponseDTO> getDocumentHistory(
    @PathVariable UUID patientId,
    @RequestParam String category,
    @RequestParam String type
  ) {
    DocumentsResponseDTO response = documentService.getDocumentHistoryByType(patientId, category, type);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<byte[]> viewDocument(
    @PathVariable UUID patientId,
    @RequestParam String path
  ) {
    byte[] file = documentService.viewPatientMedicalDocuments(patientId, path);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(file);
  }

  @Override
  public ResponseEntity<Void> deleteDocument(
    @PathVariable UUID patientId,
    @RequestParam String fileName
  ) {
    documentService.deleteDocument(patientId, fileName);
    return ResponseEntity.noContent().build();
  }
}
