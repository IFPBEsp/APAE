package br.org.apae.api.documents.interfaces.validations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class NotEmptyFilesTest {

  private final NotEmptyFiles.ListValidator validator = new NotEmptyFiles.ListValidator();

  @Test
  @DisplayName("Deve rejeitar lista vazia")
  void shouldRejectEmptyList() {
    assertFalse(validator.isValid(List.of(), null));
  }

  @Test
  @DisplayName("Deve rejeitar lista nula")
  void shouldRejectNullList() {
    assertFalse(validator.isValid(null, null));
  }

  @Test
  @DisplayName("Deve aceitar lista com ao menos um arquivo")
  void shouldAcceptListWithAtLeastOneFile() {
    MultipartFile file = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        "conteudo".getBytes());

    assertTrue(validator.isValid(List.of(file), null));
  }

  @Test
  @DisplayName("Deve rejeitar lista somente com arquivos vazios")
  void shouldRejectListWithOnlyEmptyFiles() {
    MultipartFile empty = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        new byte[0]);

    assertFalse(validator.isValid(List.of(empty), null));
  }

  @Test
  @DisplayName("SingleFileValidator deve rejeitar arquivo nulo")
  void singleShouldRejectNullFile() {
    NotEmptyFiles.SingleFileValidator single = new NotEmptyFiles.SingleFileValidator();

    assertFalse(single.isValid(null, null));
  }

  @Test
  @DisplayName("SingleFileValidator deve rejeitar arquivo vazio")
  void singleShouldRejectEmptyFile() {
    NotEmptyFiles.SingleFileValidator single = new NotEmptyFiles.SingleFileValidator();
    MultipartFile empty = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        new byte[0]);

    assertFalse(single.isValid(empty, null));
  }

  @Test
  @DisplayName("SingleFileValidator deve aceitar arquivo com conteúdo")
  void singleShouldAcceptFileWithContent() {
    NotEmptyFiles.SingleFileValidator single = new NotEmptyFiles.SingleFileValidator();
    MultipartFile file = new MockMultipartFile("file", "doc.pdf", "application/pdf",
        "conteudo".getBytes());

    assertTrue(single.isValid(file, null));
  }
}
