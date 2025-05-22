package br.org.apae.documentos_pessoais_digitalizados.api.controllers;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PersonalDocumentController {
    ResponseEntity<Void> attachPersonalDocument(
             String patientId,
             String documentType,
             MultipartFile file
    );

    ResponseEntity<PersonalDocumentResDTO> listPersonalDocuments(String patientId);

    ResponseEntity<PersonalDocumentResDTO> listPersonalDocumentByType(
            String patientId,
            String documentTpe
    );

    ResponseEntity<byte[]> viewPatientPersonalDocument(
            String patientId,
            String fileName
    );

    ResponseEntity<Void> deleteDocument(
            String patientId,
            String fileName
    );
}
