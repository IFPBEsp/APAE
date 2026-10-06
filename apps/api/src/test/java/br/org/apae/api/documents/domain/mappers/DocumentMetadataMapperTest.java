package br.org.apae.api.documents.domain.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.org.apae.api.documents.domain.builders.DocumentReferenceBuilder;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;

class DocumentMetadataMapperTest {

    private static final UUID DOCUMENT_ID = UUID.fromString("1f2e3d4c-5b6a-7988-9a0b-1c2d3e4f5061");
    private static final String OWNER = "MARIA SOUZA";
    private static final int YEAR_VALUE = 2026;
    private static final String MINIO_PREFIX = "X-Amz-Meta-";

    private static Map<String, String> minioMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put(MINIO_PREFIX + "Id", DOCUMENT_ID.toString());
        metadata.put(MINIO_PREFIX + "Category", DocumentCategory.MEDICAL.toString());
        metadata.put(MINIO_PREFIX + "Type", DocumentType.MEDICAL_REPORT.toString());
        metadata.put(MINIO_PREFIX + "Owner", OWNER);
        metadata.put(MINIO_PREFIX + "Year", String.valueOf(YEAR_VALUE));
        return metadata;
    }

    @Test
    @DisplayName("from deve gravar as cinco chaves cruas esperadas pelo MinIO")
    void from_ShouldBuildMapWithAllRawKeys() {
        Map<String, String> metadata = DocumentMetadataMapper.from(
                DOCUMENT_ID,
                DocumentCategory.MEDICAL,
                DocumentType.MEDICAL_REPORT,
                OWNER,
                Year.of(YEAR_VALUE));

        assertEquals(5, metadata.size());
        assertEquals(OWNER, metadata.get("owner"));
        assertEquals(DOCUMENT_ID.toString(), metadata.get("id"));
        assertEquals(DocumentCategory.MEDICAL.toString(), metadata.get("category"));
        assertEquals(DocumentType.MEDICAL_REPORT.toString(), metadata.get("type"));
        assertEquals(String.valueOf(YEAR_VALUE), metadata.get("year"));
    }

    @Test
    @DisplayName("from deve serializar categoria e tipo pelo valor do enum, nao pelo nome da constante")
    void from_ShouldSerializeEnumsByValue() {
        Map<String, String> metadata = DocumentMetadataMapper.from(
                DOCUMENT_ID,
                DocumentCategory.ABSENCE,
                DocumentType.ATTACHMENTANY,
                OWNER,
                Year.of(YEAR_VALUE));

        assertEquals("FALTA", metadata.get("category"));
        assertEquals("ANEXO_QUALQUER", metadata.get("type"));
        assertTrue(DocumentCategory.ABSENCE.toString().equals("FALTA"));
    }

    @Test
    @DisplayName("from de DTO deve produzir exatamente o mesmo mapa da sobrecarga de cinco argumentos")
    void fromDocumentDto_ShouldDelegateToFiveArgumentOverload() {
        DocumentDTO dto = new DocumentDTO(
                DOCUMENT_ID,
                "DOCUMENTO_MEDICO/2026/LAUDO/" + DOCUMENT_ID,
                DocumentCategory.MEDICAL,
                DocumentType.MEDICAL_REPORT,
                OWNER,
                Year.of(YEAR_VALUE));

        Map<String, String> fromDto = DocumentMetadataMapper.from(dto);
        Map<String, String> fromArguments = DocumentMetadataMapper.from(
                DOCUMENT_ID,
                DocumentCategory.MEDICAL,
                DocumentType.MEDICAL_REPORT,
                OWNER,
                Year.of(YEAR_VALUE));

        assertEquals(fromArguments, fromDto);
    }

    @Test
    @DisplayName("from de mapa deve ler as chaves prefixadas do MinIO")
    void fromMap_ShouldReadMinioPrefixedKeys() {
        DocumentDTO dto = DocumentMetadataMapper.from(minioMetadata());

        assertEquals(DOCUMENT_ID, dto.id());
        assertEquals(OWNER, dto.owner());
        assertEquals(DocumentCategory.MEDICAL, dto.category());
        assertEquals(DocumentType.MEDICAL_REPORT, dto.type());
        assertEquals(Year.of(YEAR_VALUE), dto.year());
    }

    @Test
    @DisplayName("from de mapa deve reconstruir o nome do documento com o DocumentReferenceBuilder")
    void fromMap_ShouldRebuildDocumentName() {
        Map<String, String> metadata = minioMetadata();
        metadata.put(MINIO_PREFIX + "Category", DocumentCategory.PERSONAL.toString());
        metadata.put(MINIO_PREFIX + "Type", DocumentType.RG.toString());

        DocumentDTO dto = DocumentMetadataMapper.from(metadata);

        assertEquals(
                DocumentReferenceBuilder.buildDocumentName(
                        DocumentCategory.PERSONAL,
                        DocumentType.RG,
                        Year.of(YEAR_VALUE),
                        DOCUMENT_ID),
                dto.name());
        assertEquals("DOCUMENTO_PESSOAL/2026/RG/" + DOCUMENT_ID, dto.name());
    }

    @Test
    @DisplayName("from de mapa deve recusar categoria desconhecida")
    void fromMap_ShouldThrowIllegalArgumentException_WhenCategoryIsInvalid() {
        Map<String, String> metadata = minioMetadata();
        metadata.put(MINIO_PREFIX + "Category", "CATEGORIA_INEXISTENTE");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> DocumentMetadataMapper.from(metadata));

        assertTrue(exception.getMessage().contains("CATEGORIA_INEXISTENTE"));
    }

    @Test
    @DisplayName("from de mapa deve recusar tipo desconhecido")
    void fromMap_ShouldThrowIllegalArgumentException_WhenTypeIsInvalid() {
        Map<String, String> metadata = minioMetadata();
        metadata.put(MINIO_PREFIX + "Type", "TIPO_INEXISTENTE");

        assertThrows(IllegalArgumentException.class, () -> DocumentMetadataMapper.from(metadata));
    }

    @Test
    @DisplayName("from de mapa deve recusar ano que nao e um Year valido")
    void fromMap_ShouldThrowDateTimeParseException_WhenYearIsInvalid() {
        Map<String, String> metadata = minioMetadata();
        metadata.put(MINIO_PREFIX + "Year", "ano-invalido");

        assertThrows(DateTimeParseException.class, () -> DocumentMetadataMapper.from(metadata));
    }

    @Test
    @DisplayName("from de mapa nao faz round-trip com from de cinco argumentos, que grava chaves cruas")
    void fromMap_ShouldNotRoundTripWithTheFiveArgumentOverload() {
        Map<String, String> rawKeys = DocumentMetadataMapper.from(
                DOCUMENT_ID,
                DocumentCategory.MEDICAL,
                DocumentType.MEDICAL_REPORT,
                OWNER,
                Year.of(YEAR_VALUE));

        assertThrows(NullPointerException.class, () -> DocumentMetadataMapper.from(rawKeys));
    }
}
