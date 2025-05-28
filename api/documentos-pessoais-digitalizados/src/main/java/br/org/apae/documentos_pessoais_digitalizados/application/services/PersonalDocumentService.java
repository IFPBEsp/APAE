package br.org.apae.documentos_pessoais_digitalizados.application.services;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.domain.models.PersonalDocumentType;

public interface PersonalDocumentService {
    void deleteDocument(String patientId, String fileName);

    PersonalDocumentResDTO listPersonalDocument(String patientId);

    PersonalDocumentResDTO listPersonalDocumentByType(String patientId, PersonalDocumentType type);

    void saveFile(PersonalDocumentReqDTO personalDTO);

    byte[] viewPatientPersonalDocuments(String patientId, String path);

}
