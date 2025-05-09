package br.org.apae.documentos_medicos.domain.repositories;

import java.util.List;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

public interface MinioStorage {
    /**
     * Realiza o upload do arquivo para o MinIO
     * @param dtoObject
     */ 
    void uploadFile(MedicalDocumentUploadDTO dtoObject);

    /**
     * Retorna as URLs de acesso aos arquivos
     * @param patientId UUID do paciente referente ao bucket do paciente
     * @return Lista de urls temporarias contendo a imagem dos documentos de
     * acordo com o id do paciente 
     */
    List<String> listMedicalDocumentUrls(String patientId);

    /**
     * Retorna a URLs de acesso aos arquivos
     * @param patientId UUID do paciente referente ao bucket do paciente
     * @param type Tipo do documento médico(Laudo, Encaminhamento ou Exame)
     * @return Lista de urls temporarias contendo a imagem dos documentos de
     * acordo com o id do paciente e o tipo do documento médico
     */
    List<String> listMedicalDocumentUrlsByType(String patientId, MedcialDocumentType type);

    /**
     * Retorna a imagem de um documento especifico
     * @param fileName nome do arquivo
     * @return Array de bytes que contém a imagem do documento
     */
    byte[] getMedicalDocument(String patientId, String fileName);
}
