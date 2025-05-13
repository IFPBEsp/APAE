package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public interface PersonalDocumentService {
    List<PersonalDocumentResUrlDTO> create(UUID patientId, PersonalDocumentReqDTO personalDocument);
    void delete(UUID id);
    PersonalDocumentResUrlDTO update(UUID id, PersonalDocumentFileReqDTO personalDocumentFileReqDTO);
    List<PersonalDocumentResUrlDTO> findAll(UUID patientId);
    PersonalDocumentResUrlDTO findById(UUID id);
    HashMap<String, byte[]> findFileByDocumentId(UUID id);
}
