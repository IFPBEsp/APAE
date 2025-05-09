package br.org.apae.documentos_pessoais_digitalizados.domain.repositories;

import br.org.apae.documentos_pessoais_digitalizados.domain.models.PersonalDocument;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonalDocumentRepository {
    PersonalDocument create(PersonalDocument personalDocument);
    PersonalDocument update(UUID id, PersonalDocument personalDocument);
    Optional<PersonalDocument> findById(UUID id);
    List<PersonalDocument> findAll();
}
