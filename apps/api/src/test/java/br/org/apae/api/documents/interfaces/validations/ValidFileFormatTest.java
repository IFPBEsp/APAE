package br.org.apae.api.documents.interfaces.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidatorContext;

class ValidFileFormatTest {

  private final ValidFileFormat.Validator validator = new ValidFileFormat.Validator();

  @Test
  @DisplayName("Deve aceitar extensão e content-type permitidos")
  void shouldAcceptAllowedFormat() {
    MultipartFile pdf = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        "conteudo".getBytes());
    MultipartFile png = new MockMultipartFile("file", "img.png", "image/png",
        "conteudo".getBytes());
    MultipartFile docx = new MockMultipartFile("file", "arquivo.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "conteudo".getBytes());

    assertTrue(validator.isValid(List.of(pdf, png, docx), null));
  }

  @Test
  @DisplayName("Deve rejeitar extensão não permitida")
  void shouldRejectNotAllowedExtension() {
    MultipartFile exe = new MockMultipartFile("file", "malware.exe", "application/pdf",
        "conteudo".getBytes());

    assertFalse(validator.isValid(List.of(exe), violationContext()));
  }

  @Test
  @DisplayName("Deve rejeitar content-type não permitido")
  void shouldRejectNotAllowedContentType() {
    MultipartFile txt = new MockMultipartFile("file", "notas.txt", "text/plain",
        "conteudo".getBytes());

    assertFalse(validator.isValid(List.of(txt), violationContext()));
  }

  @Test
  @DisplayName("Deve aceitar lista nula ou vazia")
  void shouldAcceptNullOrEmptyList() {
    assertTrue(validator.isValid(null, null));
    assertTrue(validator.isValid(List.of(), null));
  }

  private static ConstraintValidatorContext violationContext() {
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
        ConstraintValidatorContext.ConstraintViolationBuilder.class);
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    return context;
  }
}
