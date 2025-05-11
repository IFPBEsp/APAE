package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.exception.DocumentNotFoundException;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import br.org.apae.documentos_pessoais_digitalizados.domain.repository.PersonalDocumentRepository;
import br.org.apae.documentos_pessoais_digitalizados.infrastructure.mapper.PersonalDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
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
    public List<PersonalDocumentResUrlDTO> create(UUID patientId, PersonalDocumentReqDTO personalDocumentReqDTO) {
        List<PersonalDocument> personalDocuments = personalDocumentReqDTO.documents().stream()
                .map(document -> createPersonalDocumentAndSaveFileStorage(patientId, document))
                .toList();

        return personalDocuments.stream()
                .map(this.personalDocumentMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        this.personalDocumentRepository.delete(id);
    }

    @Override
    public PersonalDocumentResUrlDTO update(UUID id, PersonalDocumentFileReqDTO personalDocumentFileReqDTO) {
        PersonalDocument personalDocument = this.personalDocumentRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
        String bucketName = personalDocument.getPatient().toString();
        this.storageService.deleteFile(personalDocument.getPathDocumentStorage(), bucketName);
        String pathOfFile = this.storageService.uploadDocuments(personalDocumentFileReqDTO, bucketName);
        personalDocument.setPersonalDocumentType(personalDocumentFileReqDTO.personalDocumentType());
        personalDocument.setPathDocumentStorage(pathOfFile);
        return this.personalDocumentMapper.toDTO(this.personalDocumentRepository.create(personalDocument));
    }

    @Override
    public List<PersonalDocumentResUrlDTO> findAll(UUID patientId) {
        List<PersonalDocument> personalDocuments = this.personalDocumentRepository.findByPatient(patientId);
        return personalDocuments.stream().map(this.personalDocumentMapper::toDTO).toList();
    }

    @Override
    public PersonalDocumentResUrlDTO findById(UUID id) {
        PersonalDocument personalDocument = this.personalDocumentRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
        return this.personalDocumentMapper.toDTO(personalDocument);
    }

    @Override
    public PersonalDocument findFileByDocumentId(UUID id) {
        return this.personalDocumentRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
    }

    private PersonalDocument createPersonalDocumentAndSaveFileStorage(UUID patientId, PersonalDocumentFileReqDTO personalDocumentFileReqDTO) {
        String pathOfFile = this.storageService.uploadDocuments(personalDocumentFileReqDTO, patientId.toString());
        PersonalDocument personalDocument = new PersonalDocument(
                personalDocumentFileReqDTO.personalDocumentType(),
                pathOfFile,
                personalDocumentFileReqDTO.file().getContentType(),
                patientId
        );
        return this.personalDocumentRepository.create(personalDocument);
    }
}
