package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.service.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.application.service.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/personal-documents")
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;
    private final StorageService storageService;

    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService,
        StorageService storageService) {
        this.personalDocumentService = personalDocumentService;
        this.storageService = storageService;
    }

    @PostMapping
    @Override
      public ResponseEntity<List<PersonalDocumentResDTO>> create(@RequestBody PersonalDocumentReqDTO personalDocumentReqDTO) {
        List<PersonalDocumentResDTO> created = personalDocumentService.create(personalDocumentReqDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<PersonalDocumentResDTO> update(@PathVariable UUID id,
        @RequestBody PersonalDocumentReqDTO reqDTO) {
        PersonalDocumentResDTO updated = personalDocumentService.update(id, reqDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PersonalDocumentResDTO> findById(@PathVariable UUID id) {
        PersonalDocumentResDTO dto = personalDocumentService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<PersonalDocumentResDTO>> findAll() {
        List<PersonalDocumentResDTO> all = personalDocumentService.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<PersonalDocumentResDTO> delete(@PathVariable UUID id) {
        PersonalDocumentResDTO deleted = personalDocumentService.findById(id);

        //to-do
        return null;
    }

   @GetMapping("/{id}/file")
    @Override
    public ResponseEntity<byte[]> findDocumentById(@PathVariable UUID id) {
    ///to-do
    return null;
    }
}
