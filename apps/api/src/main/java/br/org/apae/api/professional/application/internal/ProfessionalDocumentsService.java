package br.org.apae.api.professional.application.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;

@Service
public class ProfessionalDocumentsService {
        private final DocumentApplicationService documentService;

        public ProfessionalDocumentsService(DocumentApplicationService documentService) {
                this.documentService = documentService;
        }

        private void storeDocument(
                        HealthProfessional professional,
                        DocumentType type,
                        MultipartFile file) {
                try {
                        this.documentService.putDocument(
                                        PutDocumentArgsDTO.builder()
                                                        .owner(professional.getId().toString())
                                                        .category(DocumentCategory.PROFESSIONAL)
                                                        .type(type)
                                                        .contentType(file.getContentType())
                                                        .stream(file.getInputStream())
                                                        .build());
                } catch (Exception error) {
                        Logger.getGlobal().log(Level.SEVERE, error.toString());
                }
        }

        public void storeProfessionalDocuments(
                        HealthProfessional professional,
                        CreateProfessionalDocumentsDTO documents) {
                storeDocument(professional, DocumentType.VOLUNTEER_AGREEMENT,
                                documents.volunteerAgreement());
                storeDocument(professional, DocumentType.CURRICULUM, documents.curriculum());
                storeDocument(professional, DocumentType.ATTACHMENTANY, documents.attachmentAny());
        }
}
 