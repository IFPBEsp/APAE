package br.org.apae.api.controllers.absence;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.appointment.interfaces.controllers.AbsenceController;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Year;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AbsenceControllerImpl implements AbsenceController {

    private final AbsenceApplicationService service;
    private final DocumentApplicationService documentApplicationService;

    public AbsenceControllerImpl(AbsenceApplicationService service, DocumentApplicationService documentService) {
        this.service = service;
        this.documentApplicationService = documentService;
    }

    @Override
    public ResponseEntity<AbsenceResponseDTO> register(CreateAbsenceDTO dto) {
        AbsenceResponseDTO response = service.register(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Override
    public ResponseEntity<Page<AbsenceResponseDTO>> findAll(
            UUID generatedId, UUID patientId, UUID professionalId, Pageable pageable) {

        Page<AbsenceResponseDTO> response = service.findAllByFilters(
                generatedId, patientId, professionalId, pageable);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> uploadDocument(
            UUID generatedAppointmentId,
            MultipartFile file,
            String type
    ) {
        try {

            DocumentCategory docCategory = DocumentCategory.ABSENCE;
            DocumentType docType = DocumentType.valueOf(type);
            Year docYear = Year.now();

            System.out.println("docCategory");
            System.out.println(docCategory);
            System.out.println("docType");
            System.out.println(docType);
            System.out.println("docYear");
            System.out.println(docYear);
            System.out.println("file.getContentType()");
            System.out.println(file.getContentType());

            PutDocumentArgsDTO args = PutDocumentArgsDTO.builder()
                    .owner(generatedAppointmentId.toString())
                    .category(docCategory)
                    .type(docType)
                    .year(docYear)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream())
                    .build();

            DocumentDTO documentDTO = this.documentApplicationService.putDocument(args);

            return ResponseEntity.status(HttpStatus.CREATED).body(documentDTO.id().toString());

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Tipo de documento inválido: " + type, e
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao fazer upload do documento de falta", e
            );
        }
    }
}