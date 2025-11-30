package br.org.apae.api.common.dto.professional.request.documents;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

public record CreateProfessionalDocumentsDTO(
                @NotNull MultipartFile volunteerAgreement,
                @NotNull MultipartFile curriculum) {
}
