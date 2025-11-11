package br.org.apae.api.controllers.patient;

import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.patient.application.interfaces.PatientDocumentsApplicationService;
import br.org.apae.api.patient.interfaces.controllers.PatientDocumentsController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

record FileResponse(String filename, String url) {}

@RestController
public class PatientDocumentsControllerImpl implements PatientDocumentsController{


    private final PatientDocumentsApplicationService patientDocumentsApplicationService;

    public PatientDocumentsControllerImpl(PatientDocumentsApplicationService patientDocumentsService) {
        this.patientDocumentsApplicationService = patientDocumentsService;
    }

    @Override
    public ResponseEntity<List<DocumentDTO>> findMedicalDocuments(UUID id) {
        return ResponseEntity.ok(patientDocumentsApplicationService.findPatientDocuments(id, DocumentCategory.MEDICAL));

    }

    @Override
    public ResponseEntity<Resource> downloadDocument(HttpServletRequest request, @PathVariable UUID id) {
        String requestURL = request.getRequestURL().toString();

        System.out.println();
        String name = requestURL.split("/download/")[1];
        System.out.println(request);
        InputStream document = this.patientDocumentsApplicationService.findPatientDocumentByName(id, name);

        Resource resource = new InputStreamResource(document);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);


    }


    @Override
    public ResponseEntity<PatientResponseDTO> findPersonalDocuments(UUID id) {
        patientDocumentsApplicationService.findPatientDocuments(id, DocumentCategory.PERSONAL);
        return null;
    }

    @Override
    public ResponseEntity<PatientResponseDTO> findSchoolDocuments(UUID id) {
        patientDocumentsApplicationService.findPatientDocuments(id, DocumentCategory.SCHOOL);
        return null;
    }
}
