package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResDTO;

import java.util.List;
import java.util.UUID;

public interface PersonalDocumentService {
    List<PersonalDocumentResDTO> create(PersonalDocumentReqDTO personalDocument);
    PersonalDocumentReqDTO delete(UUID id);
    PersonalDocumentResDTO update(UUID id, PersonalDocumentReqDTO personalDocument);
    PersonalDocumentResDTO findById(UUID id);
    List<PersonalDocumentResDTO> findAll();
}
