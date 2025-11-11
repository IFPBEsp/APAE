package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RequestMapping("/patients/{id}/documents")
@Tag(name = "Patients", description = "Endpoints para gerenciamento de pacientes")
public interface PatientDocumentsController {

    @GetMapping("/medicals")
    ResponseEntity<List<DocumentDTO>> findMedicalDocuments(@PathVariable UUID id);

    @GetMapping("/download/**")
    ResponseEntity<Resource> downloadDocument(HttpServletRequest request, @PathVariable UUID id);

    @GetMapping("/personals")
    ResponseEntity<PatientResponseDTO> findPersonalDocuments(@PathVariable UUID id);

    @GetMapping("/schools")
    ResponseEntity<PatientResponseDTO> findSchoolDocuments(@PathVariable UUID id);

}
