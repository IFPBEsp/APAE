package br.org.apae.api.common.dto.professional.request.documents;

import br.org.apae.api.documents.interfaces.validations.ValidFileFormat;
import br.org.apae.api.documents.interfaces.validations.ValidFileSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UpdateProfessionalDocumentsDTO(
        @ValidFileSize @ValidFileFormat MultipartFile volunteerAgreement,
        @ValidFileSize @ValidFileFormat MultipartFile curriculum,
        @ValidFileSize @ValidFileFormat List<MultipartFile> attachmentAny
) {}
