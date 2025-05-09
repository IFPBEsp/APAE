package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

public interface StorageService {
    void uploadDocuments(UUID patientId, List<PersonalDocumentFileReqDTO> documents);
    byte[] findDocumentByFileName(String fileName, String bucketName) throws FileNotFoundException;
}
