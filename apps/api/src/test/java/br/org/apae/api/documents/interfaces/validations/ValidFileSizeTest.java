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

  private ValidFileSize.ListValidator validator;

  @BeforeEach
  void setUp() {
    validator = new ValidFileSize.ListValidator();
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
    ValidFileSize.ListValidator custom = new ValidFileSize.ListValidator();
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

  @Test
  @DisplayName("SingleFileValidator deve aceitar arquivo dentro do limite")
  void singleShouldAcceptFileWithinLimit() {
    ValidFileSize.SingleFileValidator single = new ValidFileSize.SingleFileValidator();
    single.initialize(validFileSizeAnnotation(TEN_MB));
    MultipartFile file = new MockMultipartFile("file", "documento.pdf", "application/pdf",
        "conteudo pequeno".getBytes());

    assertTrue(single.isValid(file, null));
  }

  @Test
  @DisplayName("SingleFileValidator deve rejeitar arquivo acima do limite")
  void singleShouldRejectFileAboveLimit() {
    ValidFileSize.SingleFileValidator single = new ValidFileSize.SingleFileValidator();
    single.initialize(validFileSizeAnnotation(TEN_MB));
    byte[] bigContent = new byte[(int) TEN_MB + 1];
    MultipartFile file = new MockMultipartFile("file", "grande.pdf", "application/pdf",
        bigContent);

    assertFalse(single.isValid(file, violationContext()));
  }

  @Test
  @DisplayName("SingleFileValidator deve respeitar limite customizado via anotação")
  void singleShouldRespectCustomMaxSizeFromAnnotation() {
    ValidFileSize.SingleFileValidator single = new ValidFileSize.SingleFileValidator();
    single.initialize(validFileSizeAnnotation(5));

    MultipartFile within = new MockMultipartFile("file", "ok.pdf", "application/pdf",
        new byte[5]);
    MultipartFile above = new MockMultipartFile("file", "grande.pdf", "application/pdf",
        new byte[6]);

    assertTrue(single.isValid(within, null));
    assertFalse(single.isValid(above, violationContext()));
  }

  @Test
  @DisplayName("SingleFileValidator deve aceitar arquivo nulo ou vazio")
  void singleShouldAcceptNullOrEmptyFile() {
    ValidFileSize.SingleFileValidator single = new ValidFileSize.SingleFileValidator();
    single.initialize(validFileSizeAnnotation(TEN_MB));
    MultipartFile empty = new MockMultipartFile("file", "vazio.pdf", "application/pdf",
        new byte[0]);

    assertTrue(single.isValid(null, null));
    assertTrue(single.isValid(empty, null));
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
