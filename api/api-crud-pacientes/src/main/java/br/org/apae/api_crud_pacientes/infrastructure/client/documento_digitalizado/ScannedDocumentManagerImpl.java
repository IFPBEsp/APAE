package br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado;

import br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos.DocumentObjectRequestDTO;
import br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos.DocumentsResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class ScannedDocumentManagerImpl implements IScannedDocumentManager {

    private final RestTemplate restTemplate;

    @Value("${documento.digitalizado.url}")
    private String baseUrl;

    public ScannedDocumentManagerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public void createBucket(UUID patientId) {
        String url = baseUrl + "/bucket/" + patientId;

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                Void.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Falha ao criar bucket: " + response.getStatusCode());
        }
    }

    @Override
    public void removeBucket(UUID patientId) {

    }

    @Override
    public void saveFile(DocumentObjectRequestDTO dto, MultipartFile file) {
        String url = baseUrl + "/upload";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ObjectMapper objectMapper = new ObjectMapper();
            String documentJson = objectMapper.writeValueAsString(dto);

            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> jsonPart = new HttpEntity<>(documentJson, jsonHeaders);

            body.add("document", jsonPart);

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Falha ao salvar arquivo: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao preparar requisição de upload: " + e.getMessage(), e);
        }
    }

    @Override
    public DocumentsResponseDTO listDocument(UUID patientId, String category, Integer year) {
        return null;
    }

    @Override
    public DocumentsResponseDTO listDocumentByType(UUID patientId, Integer year, String category, String type) {
        return null;
    }

    @Override
    public DocumentsResponseDTO getDocumentHistoryByType(UUID patientId, String category, String type) {
        return null;
    }

    @Override
    public byte[] viewPatientMedicalDocuments(UUID patientId, String path) {
        return new byte[0];
    }

    @Override
    public void deleteDocument(UUID patientId, String fileName) {

    }
}
