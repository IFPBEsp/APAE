package br.org.apae.api_crud_pacientes.application.service;

import br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.IScannedDocumentManager;
import br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos.DocumentObjectRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MinIOService {
  private IScannedDocumentManager clientScannedDocumentManager;

  @Autowired
    public MinIOService(IScannedDocumentManager clientScannedDocumentManager) {
        this.clientScannedDocumentManager = clientScannedDocumentManager;
    }

    public void createBucketByPessoaID(UUID uuid, String authorizationHeader) {
      clientScannedDocumentManager.createBucket(uuid, authorizationHeader);
    }

    public void uploadDocument(DocumentObjectRequestDTO documentObjectRequestDTO, MultipartFile file, String authorizationHeader) {
      clientScannedDocumentManager.saveFile(documentObjectRequestDTO, file, authorizationHeader);
    }
}
