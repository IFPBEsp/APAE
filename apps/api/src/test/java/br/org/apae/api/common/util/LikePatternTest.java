package br.org.apae.api.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class LikePatternTest {

    @Test
    @DisplayName("Deve envolver o texto com curingas de busca parcial")
    void shouldWrapTextWithWildcards() {
        assertEquals("%ana%", LikePattern.contains("ana"));
    }

    @Test
    @DisplayName("Deve escapar os curingas do SQL LIKE")
    void shouldEscapeSqlWildcards() {
        assertEquals("%\\%%", LikePattern.contains("%"));
        assertEquals("%\\_%", LikePattern.contains("_"));
        assertEquals("%\\\\%", LikePattern.contains("\\"));
    }

    @Test
    @DisplayName("Deve escapar todos os caracteres especiais na ordem correta")
    void shouldEscapeAllSpecialCharacters() {
        assertEquals("%a\\\\b\\%c\\_d%", LikePattern.contains("a\\b%c_d"));
    }

    @Test
    @DisplayName("Deve tratar texto nulo como vazio")
    void shouldTreatNullAsEmpty() {
        assertEquals("%%", LikePattern.contains(null));
    }
}
