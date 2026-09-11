package br.org.apae.api.professional.application.interfaces;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.request.documents.UpdateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface HealthProfessionalApplicationService {

    HealthProfessionalResponseDTO createProfessional(
            CreateHealthProfessionalDTO dto,
            CreateProfessionalDocumentsDTO documentsDTO,
            MultipartFile profilePhoto
    );

    Page<HealthProfessionalResponseDTO> findAllProfessionals(Boolean ativo, Pageable pageable);

    // void deleteProfessional(UUID id);

    void activateProfessional(UUID id);

    void inactivateProfessional(UUID id);

    void reactivateProfessional(UUID id);

    HealthProfessionalResponseDTO findProfessionalById(UUID id);

    HealthProfessionalResponseDTO updateProfessional(UUID id, UpdateHealthProfessionalDTO dto);

    void updateProfessionalDocuments(UUID id, UpdateProfessionalDocumentsDTO dto);

    void removeProfessionalDocument(UUID professionalId, UUID documentId);

    void uploadProfessionalPhoto(UUID id, MultipartFile file);

    List<LocalTime> getAvailableTimes(UUID professionalId, LocalDate date);

    boolean existsByProfessionalDocumentAndIdNot(String document, UUID professionalId);
}