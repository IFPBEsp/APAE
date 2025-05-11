package br.org.apae.documentos_medicos.application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentRequestDTO;
import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@Service
public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    private MinioStorage minioMedicalDocumentStorage;
    private StorageClient storageClient;
    private static final String FOLDER_NAME = "documentos-medicos";

    @Autowired
    public MedicalDocumentServiceImpl(MinioStorage minioMedicalDocumentStorage, StorageClient storageClient) {
        this.minioMedicalDocumentStorage = minioMedicalDocumentStorage;
        this.storageClient = storageClient;
    }

    @Override
    public void saveFile(MedicalDocumentUploadDTO dtoObject, MultipartFile multipartFile) {
        // validateBucket(dtoObject.patientId());

        try {
            String bucket = dtoObject.getPatientId();
            String path = FOLDER_NAME + "/" + 
                            dtoObject.getYear() + "/" +
                            MedcialDocumentType.valueOf(dtoObject.getDocumentType()).getPrefix() + "/" + 
                            multipartFile.getOriginalFilename();
                            
            byte[] file = multipartFile.getBytes();

            minioMedicalDocumentStorage.uploadFile(bucket, path, file);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e);
        }
    }

    @Override
    public MedicalDocumentResponseDTO listMedicalDocument(String patientId, Integer year) {
        try {
            String prefix = FOLDER_NAME + "/" + year + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);

            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum documento encontrado para o paciente " + patientId + " no ano " + year);
            }

            return new MedicalDocumentResponseDTO( 
                UUID.randomUUID(), 
                objectNames.get(0), 
                "Descrição do Documento", 
                year,
                "TIPO_DOCUMENTO", 
                LocalDate.now(),
                objectNames
        );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar documentos: " + e.getMessage(), e);
        }
    }

    private void validateBucket(String bucket) {
        if (!storageClient.bucketExists(bucket)) {
            throw new RuntimeException("Bucket não existe: " + bucket);
        }
    }

    @Override
    public MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year, MedcialDocumentType type) {
        try {
            String prefix = FOLDER_NAME + "/" + year + "/" + type.name() + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);

            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum documento encontrado para o paciente " + patientId + " no ano " + year + " e tipo " + type);
            }

            return new MedicalDocumentResponseDTO(
                UUID.randomUUID(), 
                objectNames.get(0), 
                "Descrição do Documento", 
                year,
                type.name(),
                LocalDate.now(), 
                objectNames 
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar documentos do tipo: " + e.getMessage(), e);
        }
    }


    @Override
    public MedicalDocumentResponseDTO historicoTipoDocumento(String patientId, MedcialDocumentType type) {
        try {
            String prefix = FOLDER_NAME + "/" + type.name() + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);

            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum histórico encontrado para o paciente " + patientId + " e tipo " + type);
            }

            return new MedicalDocumentResponseDTO(
                UUID.randomUUID(), 
                objectNames.get(0), 
                "Histórico do Documento", 
                LocalDate.now().getYear(), 
                type.name(),
                LocalDate.now(),
                objectNames 
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar histórico de documentos: " + e.getMessage(), e);
        }
    }


    @Override
    public MedicalDocumentResponseDTO visualizarDocumentosMedicosPaciente(UUID pacienteId, UUID documentoId) {
        try {
            String objectName = FOLDER_NAME + "/" + pacienteId.toString() + "/" + documentoId.toString();
            byte[] file = minioMedicalDocumentStorage.getMedicalDocumentByFileName(pacienteId.toString(), objectName);

            if (file == null || file.length == 0) {
                throw new RuntimeException("Documento não encontrado.");
            }

            return new MedicalDocumentResponseDTO(
                documentoId,
                objectName, 
                "Documento médico", 
                LocalDate.now().getYear(), 
                "TIPO_DOCUMENTO",
                LocalDate.now(),
                List.of(objectName) 
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao visualizar o documento: " + e.getMessage(), e);
        }
    }


    @Override
    public MedicalDocumentResponseDTO atualizarDocumento(UUID pacienteId, UUID documentoId, MedicalDocumentRequestDTO documentoAtualizado, MultipartFile novoArquivo) {
        try {
            // Exemplo de como gerar o caminho para o documento
            String novoNomeArquivo = documentoId.toString() + "-" + novoArquivo.getOriginalFilename();
            String novoCaminho = FOLDER_NAME + "/" +
                             documentoAtualizado.getTipo().getPrefix() + "/" +
                             documentoAtualizado.getDataReferencia().getYear() + "/" +
                             novoNomeArquivo;

            String bucket = pacienteId.toString();

            byte[] fileBytes = novoArquivo.getBytes();
            minioMedicalDocumentStorage.uploadFile(bucket, novoCaminho, fileBytes);

            return new MedicalDocumentResponseDTO(
                documentoId,
                novoNomeArquivo,
                documentoAtualizado.getDescricao(),
                documentoAtualizado.getDataReferencia().getYear(),
                documentoAtualizado.getTipo().name(),
                LocalDate.now(),
                List.of(novoCaminho) 
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o documento: " + e.getMessage(), e);
        }
    }

   @Override
    public void desativarDocumento(UUID pacienteId, UUID documentoId) {

        //PRECISA VER SE É POSSÍVEL
        try {
            String bucket = pacienteId.toString();
            String caminhoAtivo = FOLDER_NAME + "/" + documentoId + ".pdf";

            String caminhoDesativado = FOLDER_NAME + "/inativos/" + documentoId + ".pdf";
            byte[] file = minioMedicalDocumentStorage.getMedicalDocumentByFileName(bucket, caminhoAtivo);

            if (file == null || file.length == 0) {
                throw new RuntimeException("Documento não encontrado para desativar.");
            }

            minioMedicalDocumentStorage.uploadFile(bucket, caminhoDesativado, file);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao desativar o documento: " + e.getMessage(), e);
        }
    }

}