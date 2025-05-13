package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public interface PersonalDocumentController {
    ResponseEntity<List<PersonalDocumentResUrlDTO>> create(UUID id, PersonalDocumentReqDTO personalDocumentReqDTO);
    ResponseEntity<PersonalDocumentResUrlDTO> update(UUID id, PersonalDocumentFileReqDTO personalDocumentFileReqDTO);
    ResponseEntity<PersonalDocumentResUrlDTO> findById(UUID id);
    ResponseEntity<List<PersonalDocumentResUrlDTO>> findAll(UUID patientId);
    ResponseEntity<Void> delete(UUID id);
    ResponseEntity<byte[]> findFileByDocumentId(UUID id);
}
