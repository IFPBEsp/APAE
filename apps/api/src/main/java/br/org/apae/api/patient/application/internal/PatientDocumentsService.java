package br.org.apae.api.patient.application.internal;

import java.time.Year;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.dto.RemoveDocumentArgsDTO;
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
        if (file == null || file.isEmpty()) {
            return;
        }
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
            Logger.getGlobal().log(Level.SEVERE,
                    "Falha ao salvar documento [" + category + "/" + type + "] do paciente "
                            + patient.getId() + ": " + error);
            throw new RuntimeException(
                    "Falha ao salvar documento " + type + " no armazenamento", error);
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
        if (documents.reports() != null) {
            for (MultipartFile report : documents.reports()) {
                this.storePatientDocument(patient, DocumentCategory.MEDICAL, DocumentType.MEDICAL_REPORT, report);
            }
        }

        if (documents.referrals() != null) {
            for (MultipartFile referral : documents.referrals()) {
                this.storePatientDocument(patient, DocumentCategory.MEDICAL, DocumentType.REFERRAL, referral);
            }
        }
    }

    public void storePatientDocuments(Patient patient, CreateDocumentsDTO documents) {
        this.storePersonalDocuments(patient, documents);
        this.storeMedicalDocuments(patient, documents);
    }

    public void storePatientPhoto(Patient patient, MultipartFile photo) {
        try {
            Iterable<DocumentDTO> existingPhotos = this.documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(patient.getId().toString())
                            .category(DocumentCategory.PERSONAL)
                            .type(DocumentType.PHOTO)
                            .year(Year.now())
                            .build()
            );

            for (DocumentDTO doc : existingPhotos) {
                this.documentService.removeDocument(
                        RemoveDocumentArgsDTO.builder()
                                .owner(patient.getId().toString())
                                .name(doc.name())
                                .build()
                );
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "Nao foi possivel remover foto anterior: " + e.getMessage());
        }

        this.storePatientDocument(patient, DocumentCategory.PERSONAL, DocumentType.PHOTO, photo);
    }

    public String getPatientPhoto(UUID patientId) {
        try {
            Iterable<DocumentDTO> document = this.documentService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .owner(patientId.toString())
                            .category(DocumentCategory.PERSONAL)
                            .type(DocumentType.PHOTO)
                            .year(Year.now())
                            .build()
            );

            return this.documentService.getPresignedDocumentUrl(
                    GetPresignedDocumentUrlArgsDTO.builder()
                            .owner(patientId.toString())
                            .name(document.iterator().next().name())
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );

        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e.toString());
        }
        return null;
    }
}
