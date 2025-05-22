package br.org.apae.documentos_pessoais_digitalizados.api.controllers;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.services.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.domain.models.PersonalDocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/v1/documentos-pessoais")
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;

    @Autowired
    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService) {
        this.personalDocumentService = personalDocumentService;
    }

    @Override
    @PostMapping(value = "/{patientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> attachPersonalDocument(
            @PathVariable  String patientId,
            @RequestPart String documentType,
            @RequestPart MultipartFile file
    ) {
        PersonalDocumentReqDTO dto = new PersonalDocumentReqDTO(patientId, documentType, file);
        this.personalDocumentService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/{patientId}/documentos")
    public ResponseEntity<PersonalDocumentResDTO> listPersonalDocuments(@PathVariable String patientId) {
        PersonalDocumentResDTO documentsDTO = this.personalDocumentService.listPersonalDocument(patientId);
        return ResponseEntity.ok(documentsDTO);
    }

    @Override
    @GetMapping("/{patientId}")
    public ResponseEntity<PersonalDocumentResDTO> listPersonalDocumentByType(@PathVariable String patientId, @RequestParam("tipo") String documentTpe) {
        PersonalDocumentResDTO resDTO = this.personalDocumentService.listPersonalDocumentByType(patientId, PersonalDocumentType.valueOf(documentTpe));
        return ResponseEntity.ok(resDTO);
    }

    @Override
    @GetMapping(value = "/{patientId}/vizualizar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> viewPatientPersonalDocument(@PathVariable String patientId, @RequestParam String fileName) {
        byte[] file = this.personalDocumentService.viewPatientPersonalDocuments(patientId, fileName);
        return ResponseEntity.ok(file);
    }

    @Override
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String patientId, @RequestParam String fileName) {
        this.personalDocumentService.deleteDocument(patientId, fileName);
        return ResponseEntity.noContent().build();
    }
}
