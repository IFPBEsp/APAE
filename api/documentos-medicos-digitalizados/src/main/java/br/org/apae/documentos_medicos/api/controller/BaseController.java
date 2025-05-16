package br.org.apae.documentos_medicos.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import org.springframework.web.multipart.MultipartFile;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;

public interface BaseController {

    ResponseEntity<Void> attachMedicalDocument(
        @RequestPart String patientId, 
        @RequestPart String year, 
        @RequestPart String documentType, 
        @RequestPart MultipartFile file
    );

    ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocuments(
        @PathVariable String patientId, 
        @PathVariable String year
    );

    ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocumentByType(
        @PathVariable String patientId, 
        @PathVariable String year,
        @RequestParam(required = false) String tipoDocumento
    );

    ResponseEntity<MedicalDocumentResponseDTO> listDocumentHistoryByType(
        @PathVariable String patientId, 
        @RequestParam String tipoDocumento
    );

    ResponseEntity<byte[]> viewPatientMedicalDocument(
        @PathVariable String patientId, 
        @RequestBody String fileName
    );

    ResponseEntity<Void> deleteDocument(@PathVariable String patientId, @RequestBody String fileName);
}
