package br.org.apae.api.professional.application.internal;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;
import br.org.apae.api.documents.interfaces.exceptions.InvalidResponseException;
import br.org.apae.api.professional.domain.model.HealthProfessional;

@Service
public class ProfessionalDocumentsService {
    private static final Logger logger = Logger.getLogger(ProfessionalDocumentsService.class.getName());
    private final DocumentApplicationService documentService;

    public ProfessionalDocumentsService(DocumentApplicationService documentService) {
        this.documentService = documentService;
    }

    /**
     * Armazena um documento anexo para um profissional de saúde
     * 
     * @param professional O profissional ao qual o documento pertence
     * @param file O arquivo a ser armazenado
     * @throws IOException Se ocorrer erro ao ler o arquivo
     * @throws InvalidKeyException Se a chave de acesso for inválida
     * @throws NoSuchAlgorithmException Se o algoritmo de hash não for encontrado
     * @throws InsufficientDataException Se os dados fornecidos forem insuficientes
     * @throws InvalidResponseException Se a resposta do serviço de armazenamento for inválida
     */
    private void storeProfessionalDocument(
            HealthProfessional professional,
            MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InvalidResponseException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        logger.info(String.format(
                "Iniciando upload de documento para profissional ID: %s - Arquivo: %s - Tamanho: %d bytes - Tipo: %s",
                professional.getId(), fileName, fileSize, contentType));

        try {
            this.documentService.putDocument(
                    PutDocumentArgsDTO.builder()
                            .owner(professional.getId().toString())
                            .category(DocumentCategory.PROFESSIONAL)
                            .type(DocumentType.OTHER)
                            .contentType(contentType)
                            .stream(file.getInputStream())
                            .build());

            logger.info(String.format(
                    "Upload concluído com sucesso para profissional ID: %s - Arquivo: %s",
                    professional.getId(), fileName));

        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    String.format("Erro ao ler arquivo '%s' para profissional ID: %s - Erro: %s",
                            fileName, professional.getId(), e.getMessage()),
                    e);
            throw e;
        } catch (InvalidKeyException | NoSuchAlgorithmException | InsufficientDataException
                | InvalidResponseException e) {
            logger.log(Level.SEVERE,
                    String.format("Erro ao armazenar documento '%s' para profissional ID: %s - Erro: %s",
                            fileName, professional.getId(), e.getMessage()),
                    e);
            throw e;
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    String.format("Erro inesperado ao processar documento '%s' para profissional ID: %s - Erro: %s",
                            fileName, professional.getId(), e.getMessage()),
                    e);
            throw new RuntimeException("Erro ao processar upload de documento", e);
        }
    }

    /**
     * Armazena todos os documentos anexos para um profissional de saúde
     * 
     * @param professional O profissional ao qual os documentos pertencem
     * @param documents DTO contendo a lista de arquivos a serem armazenados
     * @throws IOException Se ocorrer erro ao ler algum arquivo
     * @throws InvalidKeyException Se a chave de acesso for inválida
     * @throws NoSuchAlgorithmException Se o algoritmo de hash não for encontrado
     * @throws InsufficientDataException Se os dados fornecidos forem insuficientes
     * @throws InvalidResponseException Se a resposta do serviço de armazenamento for inválida
     */
    public void storeProfessionalDocuments(
            HealthProfessional professional,
            CreateProfessionalDocumentsDTO documents) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InvalidResponseException {
        
        if (documents == null || documents.attachments() == null || documents.attachments().isEmpty()) {
            logger.warning(String.format(
                    "Tentativa de armazenar documentos vazios para profissional ID: %s",
                    professional.getId()));
            return;
        }

        logger.info(String.format(
                "Iniciando armazenamento de %d documento(s) para profissional ID: %s",
                documents.attachments().size(), professional.getId()));

        int successCount = 0;
        int failureCount = 0;

        for (MultipartFile file : documents.attachments()) {
            if (file == null || file.isEmpty()) {
                logger.warning("Arquivo nulo ou vazio ignorado durante o upload");
                continue;
            }

            try {
                storeProfessionalDocument(professional, file);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                logger.log(Level.SEVERE,
                        String.format("Falha ao armazenar arquivo '%s' para profissional ID: %s - Erro: %s",
                                file.getOriginalFilename(), professional.getId(), e.getMessage()),
                        e);
                // Continuar com os próximos arquivos mesmo se um falhar
            }
        }

        logger.info(String.format(
                "Processamento de documentos concluído para profissional ID: %s - Sucessos: %d - Falhas: %d",
                professional.getId(), successCount, failureCount));

        if (failureCount > 0 && successCount == 0) {
            throw new RuntimeException(
                    String.format("Falha ao armazenar todos os documentos para profissional ID: %s", professional.getId()));
        }
    }
}

