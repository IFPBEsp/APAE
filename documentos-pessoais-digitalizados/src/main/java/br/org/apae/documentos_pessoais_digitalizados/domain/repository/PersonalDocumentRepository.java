package br.org.apae.documentos_pessoais_digitalizados.domain.repository;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalDocumentRepository {
    PersonalDocument create(PersonalDocument personalDocument);
    Optional<PersonalDocument> findById(UUID id);
    List<PersonalDocument> findByPatient(UUID patientId);
    void delete (UUID id);
    Optional<PersonalDocument> findByPathDocumentStorageAndPatient(String filename, UUID patient);
}
