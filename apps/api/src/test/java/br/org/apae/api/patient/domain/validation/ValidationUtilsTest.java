package br.org.apae.api.patient.domain.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.org.apae.api.patient.domain.exceptions.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

  @Test
  @DisplayName("requireNonNullOrEmpty deve lançar exceção para valor nulo")
  void requireNonNullOrEmptyShouldThrowWhenNull() {
    InvalidDataException ex = assertThrows(InvalidDataException.class,
        () -> ValidationUtils.requireNonNullOrEmpty(null, "nome"));

    assertEquals("nome não pode ser nulo ou vazio.", ex.getMessage());
  }

  @Test
  @DisplayName("requireNonNullOrEmpty deve lançar exceção para valor vazio")
  void requireNonNullOrEmptyShouldThrowWhenEmpty() {
    assertThrows(InvalidDataException.class,
        () -> ValidationUtils.requireNonNullOrEmpty("", "nome"));
  }

  @Test
  @DisplayName("requireNonNullOrEmpty deve lançar exceção para valor só com espaços")
  void requireNonNullOrEmptyShouldThrowWhenBlank() {
    assertThrows(InvalidDataException.class,
        () -> ValidationUtils.requireNonNullOrEmpty("   ", "nome"));
  }

  @Test
  @DisplayName("requireNonNullOrEmpty não deve lançar exceção para valor válido")
  void requireNonNullOrEmptyShouldNotThrowWhenValid() {
    assertDoesNotThrow(() -> ValidationUtils.requireNonNullOrEmpty("Maria", "nome"));
  }

  @Test
  @DisplayName("requireNonNull deve lançar exceção para valor nulo")
  void requireNonNullShouldThrowWhenNull() {
    InvalidDataException ex = assertThrows(InvalidDataException.class,
        () -> ValidationUtils.requireNonNull(null, "responsavel"));

    assertEquals("responsavel não pode ser nulo.", ex.getMessage());
  }

  @Test
  @DisplayName("requireNonNull não deve lançar exceção para valor não nulo")
  void requireNonNullShouldNotThrowWhenNotNull() {
    assertDoesNotThrow(() -> ValidationUtils.requireNonNull(new Object(), "responsavel"));
    assertDoesNotThrow(() -> ValidationUtils.requireNonNull("", "responsavel"));
  }
}
