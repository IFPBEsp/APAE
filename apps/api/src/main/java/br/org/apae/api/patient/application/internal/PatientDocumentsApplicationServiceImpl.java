package br.org.apae.api.patient.application.internal;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.patient.application.interfaces.PatientDocumentsApplicationService;
import com.google.common.collect.Iterables;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PatientDocumentsApplicationServiceImpl implements PatientDocumentsApplicationService {

    private final DocumentApplicationService documentApplicationService;

    public PatientDocumentsApplicationServiceImpl(DocumentApplicationService documentApplicationService) {
        this.documentApplicationService = documentApplicationService;
    }

    @Override
    public List<DocumentDTO> findPatientDocuments(UUID id, DocumentCategory documentCategory) {

        try{
            var documents = this.documentApplicationService.listDocuments(
                    ListDocumentsArgsDTO.builder()
                            .category(documentCategory)
                            .owner(id.toString())
                            .build());

            return StreamSupport.stream(documents.spliterator(), false).toList();
        }catch (Exception e){
            LoggerFactory.getLogger(PatientDocumentsApplicationServiceImpl.class).error(e.getMessage());
        }

        return null;
    }

    @Override
    public InputStream findPatientDocumentByName(UUID id, String name) {
        try{
            return this.documentApplicationService.getDocument(
                    GetDocumentArgsDTO.builder()
                            .name(name)
                            .owner(id.toString())
                            .build());
        }catch (Exception e){
            LoggerFactory.getLogger(PatientDocumentsApplicationServiceImpl.class).error(e.getMessage());
        }return null;
    }


}
