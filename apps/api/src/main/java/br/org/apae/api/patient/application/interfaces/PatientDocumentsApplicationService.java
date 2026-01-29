package br.org.apae.api.patient.application.interfaces;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface PatientDocumentsApplicationService {

    List<DocumentDTO> findPatientDocuments(UUID id, DocumentCategory documentCategory);

    InputStream findPatientDocumentByName(UUID id, String name);
}
