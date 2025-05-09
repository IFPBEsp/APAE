package br.org.apae.documentos_pessoais_digitalizados.api.controllers;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PersonalDocumentController {
    ResponseEntity<PersonalDocumentResDTO> create(PersonalDocumentReqDTO personalDocumentReqDTO);
    ResponseEntity<PersonalDocumentResDTO> update(UUID id,PersonalDocumentReqDTO personalDocumentReqDTO);
    ResponseEntity<PersonalDocumentResDTO> findById(UUID id);
    ResponseEntity<List<PersonalDocumentResDTO>> findAll();
    ResponseEntity<PersonalDocumentResDTO> delete(UUID id);
    ResponseEntity<byte[]> findDocumentById(UUID id);
}
