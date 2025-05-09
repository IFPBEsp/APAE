package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.service.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.application.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;
    private final StorageService storageService;

    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService,
        StorageService storageService) {
        this.personalDocumentService = personalDocumentService;
        this.storageService = storageService;
    }

    @Override
    public ResponseEntity<PersonalDocumentResDTO> create(PersonalDocumentReqDTO personalDocumentReqDTO) {
        // TODO: Implementar criação de documento pessoal
        return null;
    }

    @Override
    public ResponseEntity<PersonalDocumentResDTO> update(UUID id, PersonalDocumentReqDTO personalDocumentReqDTO) {
        // TODO: Implementar atualização de documento pessoal
        return null;
    }

    @Override
    public ResponseEntity<PersonalDocumentResDTO> findById(UUID id) {
        // TODO: Implementar busca por ID de documento pessoal
        return null;
    }

    @Override
    public ResponseEntity<List<PersonalDocumentResDTO>> findAll() {
        // TODO: Implementar listagem de todos os documentos pessoais
        return null;
    }

    @Override
    public ResponseEntity<PersonalDocumentResDTO> delete(UUID id) {
        // TODO: Implementar exclusão de documento pessoal
        return null;
    }

    @Override
    public ResponseEntity<byte[]> findDocumentById(UUID id) {
        // TODO: Implementar busca de arquivo de documento pelo ID
        return null;
    }
}
