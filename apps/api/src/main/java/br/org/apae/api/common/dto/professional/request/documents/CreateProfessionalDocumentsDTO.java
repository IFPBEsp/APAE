package br.org.apae.api.common.dto.professional.request.documents;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.documents.interfaces.validations.NotEmptyFiles;
import br.org.apae.api.documents.interfaces.validations.ValidFileFormat;
import br.org.apae.api.documents.interfaces.validations.ValidFileSize;
import jakarta.validation.constraints.NotNull;

public record CreateProfessionalDocumentsDTO(
        @NotNull(message = "Pelo menos um arquivo deve ser enviado")
        @NotEmptyFiles(message = "A lista de arquivos não pode estar vazia")
        @ValidFileFormat(message = "Formato de arquivo inválido. Formatos permitidos: PDF, JPG, PNG, DOCX")
        @ValidFileSize(message = "O tamanho do arquivo excede o limite máximo permitido")
        List<MultipartFile> attachments) {
}

