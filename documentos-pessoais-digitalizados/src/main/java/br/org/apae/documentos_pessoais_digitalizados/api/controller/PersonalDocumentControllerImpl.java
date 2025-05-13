package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.service.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Tag(name = "Documentos Pessoais", description = "Gerencia os documentos pessoais digitalizados dos pacientes")
@RestController
@RequestMapping("/api/documents")
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;

    @Autowired
    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService) {
        this.personalDocumentService = personalDocumentService;
    }

    @Override
    @PostMapping("paciente/{patientId}/upload")
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
        PersonalDocumentResUrlDTO personalDocumentResUrlDTO = this.personalDocumentService.update(id, personalDocumentFileReqDTO);
        return ResponseEntity.ok(personalDocumentResUrlDTO);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PersonalDocumentResUrlDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.personalDocumentService.findById(id));
    }

    @Override
    @GetMapping("paciente/{patientId}")
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> findAll(@PathVariable UUID patientId) {
        return ResponseEntity.ok(this.personalDocumentService.findAll(patientId));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.personalDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{patientId}/find")
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> findAllDocumentsByTag(@PathVariable UUID patientId, @RequestParam(value = "tag" , required = true) PersonalDocumentType documentType) {
        return ResponseEntity.ok(this.personalDocumentService.findByDocumentTag(documentType, patientId));
    }

    @Override
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> findFileByDocumentId(@PathVariable UUID id) {
        HashMap<String, byte[]> result = this.personalDocumentService.findFileByDocumentId(id);

        String contentType = result.keySet()
                .iterator()
                .next();
        byte[] file = result.get(contentType);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(file);
    }
}
