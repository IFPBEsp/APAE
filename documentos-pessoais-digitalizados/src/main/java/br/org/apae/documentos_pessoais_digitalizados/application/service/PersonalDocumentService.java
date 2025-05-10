package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PersonalDocumentService {
    List<PersonalDocumentResUrlDTO> create(UUID patientId, PersonalDocumentReqDTO personalDocument);
    void delete(UUID id);
    PersonalDocumentResUrlDTO update(UUID id, PersonalDocumentFileReqDTO personalDocumentFileReqDTO);
    List<PersonalDocumentResUrlDTO> findAll();
    PersonalDocument findFileByDocumentId(UUID id);
}
