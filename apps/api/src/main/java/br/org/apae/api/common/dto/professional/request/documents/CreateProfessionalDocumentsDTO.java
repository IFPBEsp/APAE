package br.org.apae.api.common.dto.professional.request.documents;

import br.org.apae.api.documents.interfaces.validations.ValidFileFormat;
import br.org.apae.api.documents.interfaces.validations.ValidFileSize;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

public record CreateProfessionalDocumentsDTO(
                @NotNull @ValidFileSize @ValidFileFormat MultipartFile volunteerAgreement,
                @NotNull @ValidFileSize @ValidFileFormat MultipartFile curriculum,
                @ValidFileSize @ValidFileFormat MultipartFile attachmentAny) {
}
