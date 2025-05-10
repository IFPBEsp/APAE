package br.org.apae.documentos_pessoais_digitalizados.infrastructure.persistence.jpa;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentJpaRepository extends JpaRepository<PersonalDocument, UUID> {
}
