package br.org.apae.documentos_pessoais_digitalizados.domain.services;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;

import java.util.List;
import java.util.UUID;

public interface PersonalDocumentService {
    PersonalDocumentResDTO create(PersonalDocumentReqDTO personalDocument);
    PersonalDocumentResDTO update(UUID id, PersonalDocumentReqDTO personalDocument);
    PersonalDocumentResDTO findById(UUID id);
    List<PersonalDocumentResDTO> findAll();
}
