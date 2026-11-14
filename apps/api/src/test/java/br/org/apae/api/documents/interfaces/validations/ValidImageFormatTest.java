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

class ValidImageFormatTest {

  private final ValidImageFormat.ListValidator validator = new ValidImageFormat.ListValidator();

  @Test
  @DisplayName("Deve aceitar extensão e content-type de imagem permitidos (PNG, JPG, JPEG, WEBP)")
  void shouldAcceptAllowedFormat() {
    MultipartFile png = new MockMultipartFile("file", "img.png", "image/png",
        "conteudo".getBytes());
    MultipartFile jpg = new MockMultipartFile("file", "img.jpg", "image/jpeg",
        "conteudo".getBytes());
    MultipartFile jpeg = new MockMultipartFile("file", "img.jpeg", "image/jpg",
        "conteudo".getBytes());
    MultipartFile webp = new MockMultipartFile("file", "img.webp", "image/webp",
        "conteudo".getBytes());

    assertTrue(validator.isValid(List.of(png, jpg, jpeg, webp), null));
  }

  @Test
  @DisplayName("Deve rejeitar extensão não permitida")
  void shouldRejectNotAllowedExtension() {
    MultipartFile exe = new MockMultipartFile("file", "malware.exe", "image/png",
        "conteudo".getBytes());

    assertFalse(validator.isValid(List.of(exe), violationContext()));
  }

  @Test
  @DisplayName("Deve rejeitar content-type não permitido (como PDF, DOCX e TXT)")
  void shouldRejectNotAllowedContentType() {
    MultipartFile pdf = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        "conteudo".getBytes());
    MultipartFile docx = new MockMultipartFile("file", "doc.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "conteudo".getBytes());
    MultipartFile txt = new MockMultipartFile("file", "notas.txt", "text/plain",
        "conteudo".getBytes());

    assertFalse(validator.isValid(List.of(pdf), violationContext()));
    assertFalse(validator.isValid(List.of(docx), violationContext()));
    assertFalse(validator.isValid(List.of(txt), violationContext()));
  }

  @Test
  @DisplayName("Deve aceitar lista nula ou vazia")
  void shouldAcceptNullOrEmptyList() {
    assertTrue(validator.isValid(null, null));
    assertTrue(validator.isValid(List.of(), null));
  }

  @Test
  @DisplayName("SingleFileValidator deve aceitar extensão e content-type permitidos")
  void singleShouldAcceptAllowedFormat() {
    ValidImageFormat.SingleFileValidator single = new ValidImageFormat.SingleFileValidator();
    MultipartFile png = new MockMultipartFile("file", "img.png", "image/png",
        "conteudo".getBytes());
    MultipartFile jpg = new MockMultipartFile("file", "img.jpg", "image/jpeg",
        "conteudo".getBytes());
    MultipartFile webp = new MockMultipartFile("file", "img.webp", "image/webp",
        "conteudo".getBytes());

    assertTrue(single.isValid(png, null));
    assertTrue(single.isValid(jpg, null));
    assertTrue(single.isValid(webp, null));
  }

  @Test
  @DisplayName("SingleFileValidator deve rejeitar extensão não permitida")
  void singleShouldRejectNotAllowedExtension() {
    ValidImageFormat.SingleFileValidator single = new ValidImageFormat.SingleFileValidator();
    MultipartFile pdfWithImageMime = new MockMultipartFile("file", "doc.pdf", "image/jpeg",
        "conteudo".getBytes());

    assertFalse(single.isValid(pdfWithImageMime, violationContext()));
  }

  @Test
  @DisplayName("SingleFileValidator deve rejeitar content-type não permitido (PDF, DOCX, TXT)")
  void singleShouldRejectNotAllowedContentType() {
    ValidImageFormat.SingleFileValidator single = new ValidImageFormat.SingleFileValidator();
    MultipartFile pdf = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        "conteudo".getBytes());
    MultipartFile docx = new MockMultipartFile("file", "doc.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "conteudo".getBytes());
    MultipartFile txt = new MockMultipartFile("file", "notas.txt", "text/plain",
        "conteudo".getBytes());

    assertFalse(single.isValid(pdf, violationContext()));
    assertFalse(single.isValid(docx, violationContext()));
    assertFalse(single.isValid(txt, violationContext()));
  }

  @Test
  @DisplayName("SingleFileValidator deve aceitar arquivo nulo ou vazio")
  void singleShouldAcceptNullOrEmptyFile() {
    ValidImageFormat.SingleFileValidator single = new ValidImageFormat.SingleFileValidator();
    MultipartFile empty = new MockMultipartFile("file", "img.png", "image/png",
        new byte[0]);

    assertTrue(single.isValid(null, null));
    assertTrue(single.isValid(empty, null));
  }

  private static ConstraintValidatorContext violationContext() {
    ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);
    ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(
        ConstraintValidatorContext.ConstraintViolationBuilder.class);
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    return context;
  }
}
