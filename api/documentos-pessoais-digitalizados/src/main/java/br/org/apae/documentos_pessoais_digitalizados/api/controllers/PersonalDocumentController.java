package br.org.apae.documentos_pessoais_digitalizados.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;

public interface PersonalDocumentController {
    ResponseEntity<Void> attachPersonalDocument(String patientId, String documentType, MultipartFile file);

    ResponseEntity<Void> deleteDocument(String patientId, String fileName);

    ResponseEntity<PersonalDocumentResDTO> listPersonalDocumentByType(String patientId, String documentTpe);

    ResponseEntity<PersonalDocumentResDTO> listPersonalDocuments(String patientId);

    ResponseEntity<byte[]> viewPatientPersonalDocument(String patientId, String fileName);
}
