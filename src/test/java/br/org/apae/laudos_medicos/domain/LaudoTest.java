package br.org.apae.laudos_medicos.domain;

import br.org.apae.laudos_medicos.domain.entities.Laudo;
import br.org.apae.laudos_medicos.domain.exceptions.DescricaoInvalidaException;
import br.org.apae.laudos_medicos.domain.exceptions.IdMedicoNaoEncontradoException;
import br.org.apae.laudos_medicos.domain.exceptions.IdPacienteNaoEncontradoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LaudoTest {

    @Test
    void criarEntidadeLaudo()  {
        // Arrange
        Long idLaudo = 1L;
        LocalDate dataEmissao = LocalDate.of(2025, 3, 21);
        String descricao = "Descrição";
        Long idPaciente = 2L;
        Long idMedico = 3L;

        // Act
        Laudo laudo = assertDoesNotThrow(() -> new Laudo(idLaudo, idPaciente, idMedico, dataEmissao, descricao));

        // Assert
        assertNotNull(laudo);
        assertEquals(idLaudo, laudo.getId());
        assertEquals(dataEmissao, laudo.getDataEmissao());
        assertEquals(descricao, laudo.getDescricao());
        assertEquals(idPaciente, laudo.getIdPaciente());
        assertEquals(idMedico, laudo.getIdMedico());
    }

    @Test
    void deveLancarExcecaoSeIdMedicoForNulo() {
        Long idLaudo= 1L;
        LocalDate dataEmissao = LocalDate.of(2025, 3, 21);
        String descricao = "Descrição";
        Long idPaciente = 2L;
        Long idMedico = null;


        IdMedicoNaoEncontradoException exception = assertThrows(IdMedicoNaoEncontradoException.class, () -> new Laudo(idLaudo, idPaciente, idMedico, dataEmissao, descricao));

        assertEquals("O id médico precisa ser preenchido.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeIdPacienteForNulo() {
        Long idLaudo = 1L;
        LocalDate dataEmissao = LocalDate.of(2025, 3, 21);
        String descricao = "Descrição";
        Long idPaciente = null;
        Long idMedico = 3L;

        IdPacienteNaoEncontradoException exception = assertThrows(IdPacienteNaoEncontradoException.class, () -> new Laudo(idLaudo, idPaciente, idMedico, dataEmissao, descricao));

        assertEquals("O id paciente precisa ser preenchida.", exception.getMessage());

    }

    @Test
    void deveLancarExcecaoSeDescricaoForInvalida() {
        Long idLaudo = 1L;
        LocalDate dataEmissao = LocalDate.of(2025, 3, 21);
        String descricao = "";
        Long idPaciente = 2L;
        Long idMedico = 3L;

        DescricaoInvalidaException exception = assertThrows(DescricaoInvalidaException.class, () -> new Laudo(idLaudo, idPaciente, idMedico, dataEmissao, descricao));

        assertEquals("A descrição do laudo precisa ser preenchida.", exception.getMessage());
    }
}
