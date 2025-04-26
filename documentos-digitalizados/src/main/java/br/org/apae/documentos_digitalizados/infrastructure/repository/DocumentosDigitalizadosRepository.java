package br.org.apae.documentos_digitalizados.infrastructure.repository;

import br.org.apae.documentos_digitalizados.domain.model.DocumentosDigitalizadosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentosDigitalizadosRepository extends JpaRepository<DocumentosDigitalizadosModel, Long> {
}
