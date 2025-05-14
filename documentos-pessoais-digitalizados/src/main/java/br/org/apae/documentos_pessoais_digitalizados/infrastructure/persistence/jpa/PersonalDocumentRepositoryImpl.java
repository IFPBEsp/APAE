package br.org.apae.documentos_pessoais_digitalizados.infrastructure.persistence.jpa;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;
import br.org.apae.documentos_pessoais_digitalizados.domain.repository.PersonalDocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PersonalDocumentRepositoryImpl implements PersonalDocumentRepository {

    private final DocumentJpaRepository personalDocumentJpaRepository;
    private final String folderName;

    public PersonalDocumentRepositoryImpl(DocumentJpaRepository personalDocumentJpaRepository, @Value("${minio.folder.name}") String folderName) {
        this.personalDocumentJpaRepository = personalDocumentJpaRepository;
        this.folderName = folderName;
    }

    @Override
    public PersonalDocument create(PersonalDocument personalDocument) {
        return this.personalDocumentJpaRepository.save(personalDocument);
    }

    @Override
    public Optional<PersonalDocument> findById(UUID id) {
        return this.personalDocumentJpaRepository.findById(id);
    }

    @Override
    public List<PersonalDocument> findByPatient(UUID patientId) {
        return this.personalDocumentJpaRepository.findByPatient(patientId);
    }

    @Override
    public void delete(UUID id) {
        this.personalDocumentJpaRepository.deleteById(id);
    }

    @Override
    public Optional<PersonalDocument> findByPathDocumentStorageAndPatient(String filename, UUID patient) {
        return this.personalDocumentJpaRepository.findByPathDocumentStorageAndPatient(this.folderName + "/" + filename, patient);
    }

    @Override
    public List<PersonalDocument> findByPersonalDocumentTypeAndPatient(PersonalDocumentType documentType, UUID patient) {
        return this.personalDocumentJpaRepository.findByPersonalDocumentTypeAndPatient(documentType, patient);
    }
}
