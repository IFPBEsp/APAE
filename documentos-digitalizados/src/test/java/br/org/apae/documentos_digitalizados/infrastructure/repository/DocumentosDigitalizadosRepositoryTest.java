package br.org.apae.documentos_digitalizados.infrastructure.repository;

import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DocumentosDigitalizadosRepositoryTest {

    @Autowired
    private DocumentosDigitalizadosRepository documentosDigitalizadosRepository;

    @Test
    void salvarDocumentosDigitalizados() {
        DocumentosDigitalizados documentosDigitalizados = new DocumentosDigitalizados();

        documentosDigitalizados.setPacienteId(1L);
        documentosDigitalizadosRepository.save(documentosDigitalizados);

        DocumentosDigitalizados salvo = documentosDigitalizadosRepository.findById(documentosDigitalizados.getId()).orElseThrow();
        Assertions.assertThat(salvo.getEncaminhamento()).isNotNull();
        Assertions.assertThat(salvo.getLaudoMedico()).isNotNull();
    }
}
