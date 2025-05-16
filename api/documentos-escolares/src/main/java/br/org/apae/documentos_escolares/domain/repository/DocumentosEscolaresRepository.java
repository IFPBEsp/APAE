package br.org.apae.documentos_escolares.domain.repository;

import br.org.apae.documentos_escolares.domain.model.DocumentoEscolar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentosEscolaresRepository extends JpaRepository<DocumentoEscolar, UUID> {

    List<DocumentoEscolar> findByPacienteId(UUID pacienteId);

    List<DocumentoEscolar> findByPacienteIdAndAno(UUID pacienteId, Integer ano);

    Optional<DocumentoEscolar> findByPacienteIdAndNomeArquivo(UUID pacienteId, String nomeArquivo);

    void deleteByPacienteIdAndNomeArquivo(UUID pacienteId, String nomeArquivo);
}
