package br.org.apae.api.patient.application.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.patient.domain.model.Patient;

@Service
class PatientDocumentsService {
    private final DocumentApplicationService documentService;

    public PatientDocumentsService(
            DocumentApplicationService documentService) {
        this.documentService = documentService;
    }

    private void storePatientDocument(
            Patient patient,
            DocumentCategory category,
            DocumentType type,
            MultipartFile file) {
        try {
            this.documentService.putDocument(
                    PutDocumentArgsDTO.builder()
                            .owner(patient.getId().toString())
                            .category(category)
                            .type(type)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream())
                            .build());

        } catch (Exception error) {
            Logger.getGlobal().log(Level.SEVERE, error.toString());
        }
    }

    private void storePersonalDocuments(Patient patient, CreateDocumentsDTO documents) {
        this.storePatientDocument(patient, DocumentCategory.PERSONAL, DocumentType.RG, documents.rg());
        this.storePatientDocument(patient, DocumentCategory.PERSONAL, DocumentType.CPF, documents.cpf());
        this.storePatientDocument(patient, DocumentCategory.PERSONAL, DocumentType.PROOF_OF_ADDRESS,
                documents.proof_of_address());
        this.storePatientDocument(patient, DocumentCategory.PERSONAL, DocumentType.BIRTH_CERTIFICATE,
                documents.birth_certificate());
        this.storePatientDocument(patient, DocumentCategory.PERSONAL, DocumentType.PHOTO, documents.photo());
    }

    private void storeMedicalDocuments(Patient patient, CreateDocumentsDTO documents) {
        for (MultipartFile report : documents.reports()) {
            this.storePatientDocument(patient, DocumentCategory.MEDICAL, DocumentType.MEDICAL_REPORT, report);
        }

        for (MultipartFile referral : documents.referrals()) {
            this.storePatientDocument(patient, DocumentCategory.MEDICAL, DocumentType.REFERRAL, referral);
        }
    }

    public void storePatientDocuments(Patient patient, CreateDocumentsDTO documents) {
        this.storePersonalDocuments(patient, documents);
        this.storeMedicalDocuments(patient, documents);
    }
}
