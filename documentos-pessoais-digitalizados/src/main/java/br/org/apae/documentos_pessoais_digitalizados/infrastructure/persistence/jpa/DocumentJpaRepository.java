package br.org.apae.documentos_pessoais_digitalizados.infrastructure.persistence.jpa;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentJpaRepository extends JpaRepository<PersonalDocument, UUID> {
    List<PersonalDocument> findByPatient(UUID patientId);
    Optional<PersonalDocument> findByPathDocumentStorageAndPatient(String filename, UUID patient);
    List<PersonalDocument> findByPersonalDocumentTypeAndPatient(PersonalDocumentType documentType, UUID patient);
}
