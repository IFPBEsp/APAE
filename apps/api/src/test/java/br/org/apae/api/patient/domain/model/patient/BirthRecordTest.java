package br.org.apae.api.patient.domain.model.patient;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import br.org.apae.api.patient.domain.exceptions.InvalidDataException;

class BirthRecordTest {

  private static final String CERTIDAO_32_DIGITOS = "12345678901234567890123456789012";
  private static final String CERTIDAO_32_DIGITOS_COM_ESPACOS = "123456 78 90 1234 5 67890 123 4567890 12";
  private static final String CERTIDAO_MENOS_32_DIGITOS = "123456";

  @Nested
  @DisplayName("Regra 1: Certidão com menos de 32 dígitos (exige cartório, fls e livro)")
  class CertidaoComMenosDe32Digitos {

    @Test
    @DisplayName("Deve criar BirthRecord quando todos os campos obrigatórios forem informados")
    void deveCriarComSucessoQuandoTodosCamposInformados() {
      BirthRecord record = new BirthRecord(
          CERTIDAO_MENOS_32_DIGITOS, "1º Cartório de Registro Civil", "45", "B-12", LocalDate.now()
      );

      assertEquals(CERTIDAO_MENOS_32_DIGITOS, record.getBirthCertificateNumber());
      assertEquals("1º Cartório de Registro Civil", record.getRegistryOffice());
      assertEquals("45", record.getFls());
      assertEquals("B-12", record.getBook());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando cartório for nulo, vazio ou em branco")
    void deveLancarExcecaoQuandoCartorioInvalido(String invalidRegistryOffice) {
      InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
          new BirthRecord(CERTIDAO_MENOS_32_DIGITOS, invalidRegistryOffice, "45", "B-12", LocalDate.now())
      );
      assertEquals("Cartório não pode ser nulo ou vazio.", ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando fls for nulo, vazio ou em branco")
    void deveLancarExcecaoQuandoFlsInvalido(String invalidFls) {
      InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
          new BirthRecord(CERTIDAO_MENOS_32_DIGITOS, "Cartório", invalidFls, "B-12", LocalDate.now())
      );
      assertEquals("FLS não pode ser nulo ou vazio.", ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando livro for nulo, vazio ou em branco")
    void deveLancarExcecaoQuandoLivroInvalido(String invalidBook) {
      InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
          new BirthRecord(CERTIDAO_MENOS_32_DIGITOS, "Cartório", "45", invalidBook, LocalDate.now())
      );
      assertEquals("Livro não pode ser nulo ou vazio.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve considerar espaços como vazios ao contar dígitos da certidão curta")
    void deveContarEspacosComoVaziosParaCertidaoCurta() {
      // String com 6 dígitos e vários espaços totalizando 15 caracteres
      String certidaoComEspacos = " 123 456  ";
      assertThrows(InvalidDataException.class, () ->
          new BirthRecord(certidaoComEspacos, null, null, null, LocalDate.now())
      );
    }
  }

  @Nested
  @DisplayName("Regra 2: Certidão com 32 dígitos (não valida cartório, fls e livro)")
  class CertidaoCom32Digitos {

    @Test
    @DisplayName("Deve aceitar cartório, fls e livro nulos quando certidão tiver 32 dígitos sem máscara")
    void deveAceitarCamposNulosCom32DigitosSemMascara() {
      BirthRecord record = assertDoesNotThrow(() ->
          new BirthRecord(CERTIDAO_32_DIGITOS, null, null, null, LocalDate.now())
      );

      assertEquals(CERTIDAO_32_DIGITOS, record.getBirthCertificateNumber());
      assertNull(record.getRegistryOffice());
      assertNull(record.getFls());
      assertNull(record.getBook());
    }

    @Test
    @DisplayName("Deve aceitar cartório, fls e livro nulos quando certidão tiver 32 dígitos com espaços (máscara)")
    void deveAceitarCamposNulosCom32DigitosComEspacos() {
      BirthRecord record = assertDoesNotThrow(() ->
          new BirthRecord(CERTIDAO_32_DIGITOS_COM_ESPACOS, null, null, null, LocalDate.now())
      );

      assertEquals(CERTIDAO_32_DIGITOS_COM_ESPACOS, record.getBirthCertificateNumber());
      assertNull(record.getRegistryOffice());
      assertNull(record.getFls());
      assertNull(record.getBook());
    }

    @Test
    @DisplayName("Deve aceitar cartório, fls e livro vazios quando certidão tiver 32 dígitos")
    void deveAceitarCamposVaziosCom32Digitos() {
      BirthRecord record = assertDoesNotThrow(() ->
          new BirthRecord(CERTIDAO_32_DIGITOS, "", "", "", LocalDate.now())
      );

      assertEquals("", record.getRegistryOffice());
      assertEquals("", record.getFls());
      assertEquals("", record.getBook());
    }

    @Test
    @DisplayName("Deve atribuir valores de cartório, fls e livro se fornecidos mesmo com certidão de 32 dígitos")
    void deveAtribuirValoresQuandoFornecidosCom32Digitos() {
      BirthRecord record = assertDoesNotThrow(() ->
          new BirthRecord(CERTIDAO_32_DIGITOS, "Cartório", "10", "A", LocalDate.now())
      );

      assertEquals("Cartório", record.getRegistryOffice());
      assertEquals("10", record.getFls());
      assertEquals("A", record.getBook());
    }
  }

  @Nested
  @DisplayName("Validações gerais de data e número de certidão")
  class ValidacoesGerais {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando número do registro de nascimento for nulo ou vazio")
    void deveLancarExcecaoQuandoNumeroRegistroInvalido(String invalidCertNumber) {
      InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
          new BirthRecord(invalidCertNumber, "Cartório", "10", "A", LocalDate.now())
      );
      assertEquals("Número do registro de nascimento não pode ser nulo ou vazio.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando data de registro for nula")
    void deveLancarExcecaoQuandoDataRegistroNula() {
      InvalidDataException ex = assertThrows(InvalidDataException.class, () ->
          new BirthRecord(CERTIDAO_32_DIGITOS, null, null, null, null)
      );
      assertEquals("Data de registro não pode ser nulo.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando data de registro for no futuro")
    void deveLancarExcecaoQuandoDataRegistroNoFuturo() {
      LocalDate dataFutura = LocalDate.now().plusDays(1);
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
          new BirthRecord(CERTIDAO_32_DIGITOS, null, null, null, dataFutura)
      );
      assertEquals("Data de registro não pode ser no futuro: " + dataFutura, ex.getMessage());
    }
  }
}
