package br.org.apae.documentos_medicos.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.application.MedicalDocumentService;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

@RestController
@RequestMapping("/api/v1/documentos-medicos")
public class DocumentoMedicoControllerImp implements BaseController {

    private final MedicalDocumentService medicalDocumentService;

    @Autowired
    public DocumentoMedicoControllerImp(MedicalDocumentService medicalDocumentService) {
        this.medicalDocumentService = medicalDocumentService;
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> attachMedicalDocument(
            @RequestPart String patientId,
            @RequestPart String year,
            @RequestPart String documentType,
            @RequestPart MultipartFile file) {

        var data = new MedicalDocumentUploadDTO(patientId, Integer.parseInt(year), documentType);
        medicalDocumentService.saveFile(data, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/{patientId}/documentos/{year}")
    public ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocuments(
            @PathVariable String patientId,
            @PathVariable String year) {

        var documents = medicalDocumentService.listMedicalDocument(patientId, Integer.parseInt(year));
        if (documents.urls().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(documents);
    }

    @Override
    @GetMapping(value = "/{patientId}/filtrar", params = {"ano", "tipo"})
    public ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocumentByType(
            @PathVariable String patientId,
            @RequestParam("ano") String year,
            @RequestParam("tipo") String type) {

        var result = medicalDocumentService.listMedicalDocumentByType(patientId, Integer.valueOf(year), MedcialDocumentType.valueOf(type.toUpperCase()));
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping("/{patientId}/historico")
    public ResponseEntity<MedicalDocumentResponseDTO> listDocumentHistoryByType(
            @PathVariable String patientId,
            @RequestParam("tipo") String type) {

        var response = medicalDocumentService.getDocumentHistoryByType(patientId, MedcialDocumentType.valueOf(type.toUpperCase()));
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping(value = "/{patientId}/visualizar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> viewPatientMedicalDocument(
            @PathVariable String patientId,
            @RequestParam String fileName) {

        var response = medicalDocumentService.viewPatientMedicalDocuments(patientId, fileName);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable String patientId,
            @RequestParam(required = true) String fileName) {

            medicalDocumentService.deleteDocument(patientId, fileName);
            return ResponseEntity.noContent().build();
    }
}
