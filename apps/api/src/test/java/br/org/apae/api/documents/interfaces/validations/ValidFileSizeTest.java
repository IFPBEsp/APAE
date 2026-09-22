package br.org.apae.api.documents.interfaces.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

class ValidFileSizeTest {

  private static final long TEN_MB = 10L * 1024 * 1024;

  private ValidFileSize.Validator validator;

  @BeforeEach
  void setUp() {
    validator = new ValidFileSize.Validator();
    validator.initialize(validFileSizeAnnotation(TEN_MB));
  }

  @Test
  @DisplayName("Deve aceitar arquivo dentro do limite")
  void shouldAcceptFileWithinLimit() {
    MultipartFile file = new MockMultipartFile("file", "documento.pdf", "application/pdf",
        "conteudo pequeno".getBytes());

    assertTrue(validator.isValid(List.of(file), null));
  }

  @Test
  @DisplayName("Deve rejeitar arquivo acima do limite")
  void shouldRejectFileAboveLimit() {
    byte[] bigContent = new byte[(int) TEN_MB + 1];
    MultipartFile file = new MockMultipartFile("file", "grande.pdf", "application/pdf",
        bigContent);

    assertFalse(validator.isValid(List.of(file), violationContext()));
  }

  @Test
  @DisplayName("Deve respeitar limite customizado via anotação")
  void shouldRespectCustomMaxSizeFromAnnotation() {
    ValidFileSize.Validator custom = new ValidFileSize.Validator();
    custom.initialize(validFileSizeAnnotation(5));

    MultipartFile within = new MockMultipartFile("file", "ok.pdf", "application/pdf",
        new byte[5]);
    MultipartFile above = new MockMultipartFile("file", "grande.pdf", "application/pdf",
        new byte[6]);

    assertTrue(custom.isValid(List.of(within), null));
    assertFalse(custom.isValid(List.of(above), violationContext()));
  }

  @Test
  @DisplayName("Deve aceitar lista nula ou vazia")
  void shouldAcceptNullOrEmptyList() {
    assertTrue(validator.isValid(null, null));
    assertTrue(validator.isValid(List.of(), null));
  }

  private static ValidFileSize validFileSizeAnnotation(long maxSize) {
    return new ValidFileSize() {
      @Override
      public String message() {
        return "O tamanho do arquivo excede o limite máximo permitido";
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
      public long maxSize() {
        return maxSize;
      }

      @Override
      public Class<? extends java.lang.annotation.Annotation> annotationType() {
        return ValidFileSize.class;
      }
    };
  }

  private static ConstraintValidatorContext violationContext() {
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
        ConstraintValidatorContext.ConstraintViolationBuilder.class);
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    return context;
  }
}
