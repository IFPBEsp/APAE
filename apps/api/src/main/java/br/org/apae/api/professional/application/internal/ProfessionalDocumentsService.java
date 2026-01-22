package br.org.apae.api.professional.application.internal;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.request.documents.UpdateProfessionalDocumentsDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.RemoveDocumentArgsDTO;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentNotFoundException;
import br.org.apae.api.professional.domain.model.HealthProfessional;

@Service
public class ProfessionalDocumentsService {

    private static final Logger LOGGER = Logger.getGlobal();

    private final DocumentApplicationService documentService;

    public ProfessionalDocumentsService(DocumentApplicationService documentService) {
        this.documentService = documentService;
    }

    private void storeDocument(
            HealthProfessional professional,
            DocumentType type,
            MultipartFile file) {

        if (file == null || file.isEmpty()) return;

        try {
            this.documentService.putDocument(
                    PutDocumentArgsDTO.builder()
                            .owner(professional.getId().toString())
                            .category(DocumentCategory.PROFESSIONAL)
                            .type(type)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream())
                            .build()
            );
        } catch (Exception error) {
            LOGGER.log(Level.SEVERE, "Erro ao armazenar documento (" + type + "): " + error.getMessage());
        }
    }

    public void storeProfessionalDocuments(
            HealthProfessional professional,
            CreateProfessionalDocumentsDTO documents) {

        storeDocument(professional, DocumentType.VOLUNTEER_AGREEMENT, documents.volunteerAgreement());
        storeDocument(professional, DocumentType.CURRICULUM, documents.curriculum());

        storeDocument(professional, DocumentType.ATTACHMENTANY, documents.attachmentAny());
    }

    public void updateProfessionalDocuments(
            HealthProfessional professional,
            UpdateProfessionalDocumentsDTO documents) {

        if (documents == null) return;

        storeDocument(professional, DocumentType.CURRICULUM, documents.curriculum());
        storeDocument(professional, DocumentType.VOLUNTEER_AGREEMENT, documents.volunteerAgreement());

        if (documents.attachmentAny() != null) {
            for (MultipartFile file : documents.attachmentAny()) {
                storeDocument(professional, DocumentType.ATTACHMENTANY, file);
            }
        }
    }

    public void removeProfessionalDocument(HealthProfessional professional, UUID documentId) {
        try {
            Iterable<DocumentDTO> docs = this.documentService.listDocuments(
                ListDocumentsArgsDTO.builder()
                    .owner(professional.getId().toString())
                    .category(DocumentCategory.PROFESSIONAL)
                    .build()
            );

            DocumentDTO target = null;
            for (DocumentDTO d : docs) {
                if (d != null && Objects.equals(d.id(), documentId)) {
                    target = d;
                    break;
                }
            }

            if (target == null) {
                throw new ProfessionalDocumentNotFoundException();
            }

            this.documentService.removeDocument(
                RemoveDocumentArgsDTO.builder()
                    .id(target.id())
                    .name(target.name())
                    .owner(target.owner())
                    .category(target.category())
                    .type(target.type())
                    .year(target.year())
                    .build()
            );
        } catch (ProfessionalDocumentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao remover documento do profissional: " + e.getMessage());
            throw new RuntimeException("Erro ao remover documento do profissional.");
        }
    }
}