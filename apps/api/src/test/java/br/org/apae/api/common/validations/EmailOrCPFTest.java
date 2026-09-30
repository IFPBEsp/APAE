package br.org.apae.api.common.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern.Flag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailOrCPFTest {

  private EmailOrCPF.Validator validator;

  @BeforeEach
  void setUp() {
    validator = new EmailOrCPF.Validator();
    validator.initialize(emailOrCpfAnnotation(true));
  }

  @Test
  @DisplayName("Deve aceitar e-mail válido")
  void shouldAcceptValidEmail() {
    assertTrue(validator.isValid("usuario@example.com", null));
    assertTrue(validator.isValid("nome.sobrenome@dominio.com.br", null));
  }

  @Test
  @DisplayName("Deve aceitar CPF válido")
  void shouldAcceptValidCpf() {
    assertTrue(validator.isValid("529.982.247-25", null));
    assertTrue(validator.isValid("12345678909", null));
  }

  @Test
  @DisplayName("Deve rejeitar valor que não é nem e-mail nem CPF")
  void shouldRejectValueThatIsNeitherEmailNorCpf() {
    assertFalse(validator.isValid("abc123", null));
    assertFalse(validator.isValid("apenas um texto", null));
    assertFalse(validator.isValid("@example.com", null));
    assertFalse(validator.isValid("user@@example.com", null));
  }

  @Test
  @DisplayName("Deve aceitar campo nulo ou vazio")
  void shouldAcceptNullOrEmptyValue() {
    assertTrue(validator.isValid(null, null));
    assertTrue(validator.isValid("", null));
  }

  @Test
  @DisplayName("Deve rejeitar CPF com dígito verificador errado quando checkCpfDigits é verdadeiro")
  void shouldRejectCpfWithWrongCheckDigits() {
    assertFalse(validator.isValid("529.982.247-26", null));
  }

  private static EmailOrCPF emailOrCpfAnnotation(boolean checkCpfDigits) {
    return new EmailOrCPF() {
      @Override
      public String message() {
        return "Informe email ou cpf válido";
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
      public String emailRegexp() {
        return ".*";
      }

      @Override
      public Flag[] emailFlags() {
        return new Flag[0];
      }

      @Override
      public boolean checkCpfDigits() {
        return checkCpfDigits;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return EmailOrCPF.class;
      }
    };
  }
}
