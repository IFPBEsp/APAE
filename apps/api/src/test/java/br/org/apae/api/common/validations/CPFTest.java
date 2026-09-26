package br.org.apae.api.common.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CPFTest {

  private CPF.Validator validator;

  @BeforeEach
  void setUp() {
    validator = new CPF.Validator();
    validator.initialize(cpfAnnotation(true));
  }

  @Test
  @DisplayName("Deve aceitar CPF válido")
  void shouldAcceptValidCpf() {
    assertTrue(validator.isValid("529.982.247-25", null));
    assertTrue(validator.isValid("52998224725", null));
  }

  @Test
  @DisplayName("Deve rejeitar CPF com todos os dígitos iguais")
  void shouldRejectCpfWithAllSameDigits() {
    assertFalse(validator.isValid("111.111.111-11", null));
    assertFalse(validator.isValid("000.000.000-00", null));
  }

  @Test
  @DisplayName("Deve rejeitar CPF com dígito verificador errado")
  void shouldRejectCpfWithWrongCheckDigits() {
    assertFalse(validator.isValid("529.982.247-26", null));
    assertFalse(validator.isValid("123.456.789-08", null));
  }

  @Test
  @DisplayName("Deve rejeitar CPF com tamanho errado")
  void shouldRejectCpfWithWrongLength() {
    assertFalse(validator.isValid("1234567890", null));
    assertFalse(validator.isValid("529.982.247-250", null));
    assertFalse(validator.isValid("abc", null));
  }

  @Test
  @DisplayName("Deve aceitar campo nulo ou vazio")
  void shouldAcceptNullOrEmptyValue() {
    assertTrue(validator.isValid(null, null));
    assertTrue(validator.isValid("", null));
  }

  @Test
  @DisplayName("Deve ignorar dígitos verificadores quando checkDigits é falso")
  void shouldSkipCheckDigitsWhenDisabled() {
    CPF.Validator noCheck = new CPF.Validator();
    noCheck.initialize(cpfAnnotation(false));

    assertTrue(noCheck.isValid("529.982.247-26", null));
    assertFalse(noCheck.isValid("1234567890", null));
  }

  private static CPF cpfAnnotation(boolean checkDigits) {
    return new CPF() {
      @Override
      public String message() {
        return "CPF inválido";
      }

      @Override
      public Class<?>[] groups() {
        return new Class<?>[0];
      }

      @Override
      public Class<? extends Payload>[] payload() {
        return new Class[0];
      }

      @Override
      public boolean checkDigits() {
        return checkDigits;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return CPF.class;
      }
    };
  }
}
