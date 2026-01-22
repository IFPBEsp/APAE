package br.org.apae.api.common.dto.professional.request.documents;

import org.springframework.web.multipart.MultipartFile;

public record UpdateProfessionalDocumentsDTO(
        MultipartFile volunteerAgreement,
        MultipartFile curriculum,
        MultipartFile[] attachmentAny
) {}
