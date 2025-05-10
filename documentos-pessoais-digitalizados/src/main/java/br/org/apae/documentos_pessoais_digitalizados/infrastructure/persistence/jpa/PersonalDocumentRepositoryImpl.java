package br.org.apae.documentos_pessoais_digitalizados.infrastructure.persistence.jpa;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import br.org.apae.documentos_pessoais_digitalizados.domain.repository.PersonalDocumentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PersonalDocumentRepositoryImpl implements PersonalDocumentRepository {

    private final DocumentJpaRepository personalDocumentJpaRepository;

    public PersonalDocumentRepositoryImpl(DocumentJpaRepository personalDocumentJpaRepository) {
        this.personalDocumentJpaRepository = personalDocumentJpaRepository;
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
    public List<PersonalDocument> findAll() {
        return this.personalDocumentJpaRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        this.personalDocumentJpaRepository.deleteById(id);
    }
}
