package br.org.apae.api.common.dto.patient.request.documents;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.documents.interfaces.validations.NotEmptyFiles;
import jakarta.validation.constraints.NotNull;

public record CreateDocumentsDTO(
        @NotNull MultipartFile rg,
        @NotNull MultipartFile cpf,
        @NotNull MultipartFile proof_of_address,
        @NotNull MultipartFile birth_certificate,
        @NotNull MultipartFile photo,
        @NotEmptyFiles List<MultipartFile> reports,
        @NotEmptyFiles List<MultipartFile> referrals) {
}
