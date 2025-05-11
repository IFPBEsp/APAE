package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.service.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.application.service.StorageService;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;
    private final StorageService storageService;

    @Autowired
    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService, StorageService storageService) {
        this.personalDocumentService = personalDocumentService;
        this.storageService = storageService;
    }

    @Override
    @PostMapping("/{patientId}/upload")
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> create(
            @Valid @PathVariable UUID patientId,
            @Valid @ModelAttribute PersonalDocumentReqDTO personalDocumentReqDTO
    ) {
        List<PersonalDocumentResUrlDTO> personalDocumentResUrlDTOS = this.personalDocumentService.create(patientId, personalDocumentReqDTO);
        return new ResponseEntity<>(personalDocumentResUrlDTOS, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PersonalDocumentResUrlDTO> update(
            @Valid @PathVariable UUID id,
            @Valid @ModelAttribute PersonalDocumentFileReqDTO personalDocumentFileReqDTO
    ) {
        PersonalDocumentResUrlDTO  personalDocumentResUrlDTO = this.personalDocumentService.update(id, personalDocumentFileReqDTO);
        return ResponseEntity.ok(personalDocumentResUrlDTO);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PersonalDocumentResUrlDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.personalDocumentService.findById(id));
    }

    @Override
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> findAll(@PathVariable UUID id) {
        return ResponseEntity.ok(this.personalDocumentService.findAll(id));
    }

    @Override
    public ResponseEntity<PersonalDocumentResUrlDTO> delete(UUID id) {
        this.personalDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> findFileByDocumentId(@PathVariable UUID id) {
        PersonalDocument personalDocument = this.personalDocumentService.findFileByDocumentId(id);
        byte[] file = this.storageService.findDocumentByFileName(personalDocument.getPathDocumentStorage(), personalDocument.getPatient().toString());
        String contentType = personalDocument.getContentType();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(file);
    }
}
