package br.org.apae.documentos_pessoais_digitalizados.domain.services;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentFileReqDTO;

import java.util.List;
import java.util.UUID;

public interface StorageService {
    String uploadDocuments(List<PersonalDocumentFileReqDTO> documents);
    byte[] findDocumentById(UUID id);
}
