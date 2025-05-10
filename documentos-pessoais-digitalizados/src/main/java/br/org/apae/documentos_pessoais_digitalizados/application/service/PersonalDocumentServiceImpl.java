package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import br.org.apae.documentos_pessoais_digitalizados.domain.repository.PersonalDocumentRepository;
import br.org.apae.documentos_pessoais_digitalizados.infrastructure.mapper.PersonalDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class PersonalDocumentServiceImpl implements PersonalDocumentService{

    private final PersonalDocumentRepository personalDocumentRepository;
    private final PersonalDocumentMapper personalDocumentMapper;
    private final StorageService storageService;

    @Autowired
    public PersonalDocumentServiceImpl(PersonalDocumentRepository personalDocumentRepository, PersonalDocumentMapper personalDocumentMapper, StorageService storageService) {
        this.personalDocumentRepository = personalDocumentRepository;
        this.personalDocumentMapper = personalDocumentMapper;
        this.storageService = storageService;
    }

    @Override
    public List<PersonalDocumentResDTO> create(PersonalDocumentReqDTO personalDocumentReqDTO) {

        return null;
    }

    @Override
    public PersonalDocumentResDTO delete(UUID id) {
    PersonalDocument personalDocument = this.personalDocumentRepository.findById(id).orElseThrow();
    PersonalDocument deletedDocument = this.personalDocumentRepository.delete(id);
    return this.personalDocumentMapper.toDTO(deletedDocument);
    }


    @Override
    public PersonalDocumentResDTO update(UUID id, PersonalDocumentReqDTO personalDocument) {
        return null;
    }

    @Override
    public PersonalDocumentResDTO findById(UUID id) {
        PersonalDocument personalDocument = this.personalDocumentRepository.findById(id).orElseThrow();
        return this.personalDocumentMapper.toDTO(personalDocument);
    }

    @Override
    public List<PersonalDocumentResDTO> findAll() {
        List<PersonalDocument> personalDocuments = this.personalDocumentRepository.findAll();
        return personalDocuments.stream()
                .map(this.personalDocumentMapper::toDTO)
                .toList();
    }

    private String createPersonalDocumentAndSaveFileStorage(UUID patientId, PersonalDocumentFileReqDTO personalDocumentFileReqDTO) {
        String pathOfFile = this.storageService.uploadDocuments(personalDocumentFileReqDTO, patientId.toString());
        PersonalDocument personalDocument = new PersonalDocument(
                personalDocumentFileReqDTO.personalDocumentType(),
                pathOfFile,
                personalDocumentFileReqDTO.file().getContentType(),
                patientId
        );
        return  "/" + pathOfFile;
    }
}
