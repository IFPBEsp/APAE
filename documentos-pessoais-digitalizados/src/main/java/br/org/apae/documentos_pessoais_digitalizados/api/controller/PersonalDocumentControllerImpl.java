package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.service.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.application.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            @PathVariable UUID patientId,
            @ModelAttribute PersonalDocumentReqDTO personalDocumentReqDTO
    ) {
        List<PersonalDocumentResUrlDTO> personalDocumentResUrlDTOS = this.personalDocumentService.create(patientId, personalDocumentReqDTO);
        return new ResponseEntity<>(personalDocumentResUrlDTOS, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PersonalDocumentResUrlDTO> update(UUID id, PersonalDocumentReqDTO personalDocumentReqDTO) {
        return null;
    }

    @Override
    public ResponseEntity<PersonalDocumentResUrlDTO> findById(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<PersonalDocumentResUrlDTO> delete(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> findFileByDocumentId(UUID id) {
        return null;
    }
}
