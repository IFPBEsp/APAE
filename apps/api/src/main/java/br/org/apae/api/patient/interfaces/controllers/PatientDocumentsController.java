package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.response.documents.DocumentWithUrlResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/patients/{id}/documents")
@Tag(name = "Patients", description = "Endpoints para gerenciamento de pacientes")
public interface PatientDocumentsController {

    @GetMapping("/medicals")
    ResponseEntity<List<DocumentWithUrlResponseDTO>> findMedicalDocuments(@PathVariable UUID id);

    @GetMapping("/personals")
    ResponseEntity<List<DocumentWithUrlResponseDTO>> findPersonalDocuments(@PathVariable UUID id);

    @GetMapping("/schools")
    ResponseEntity<List<DocumentWithUrlResponseDTO>> findSchoolDocuments(@PathVariable UUID id);

}
