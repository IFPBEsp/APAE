package br.org.apae.documentos_escolares.domain.repository;

import br.org.apae.documentos_escolares.domain.model.DocumentosEscolares;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentosEscolaresRepository extends JpaRepository<DocumentosEscolares, UUID> {

    List<DocumentosEscolares> findByPacienteId(UUID pacienteId);

    List<DocumentosEscolares> findByPacienteIdAndAno(UUID pacienteId, Integer ano);

    Optional<DocumentosEscolares> findByPacienteIdAndNomeArquivo(UUID pacienteId, String nomeArquivo);

    void deleteByPacienteIdAndNomeArquivo(UUID pacienteId, String nomeArquivo);
}
