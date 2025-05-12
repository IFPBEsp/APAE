package br.org.apae.documentos_medicos.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import org.springframework.web.multipart.MultipartFile;


import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

@RequestMapping("/api/v1/documentos-medicos")
public interface BaseController {

    /*
    * POST (/api/pacientes/{pacienteId}/documentos-medicos) - Anexar documentos médicos a um paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos) - Listar todos os documentos médicos associados a 1 paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos?tipo={tipo}) - Filtrar por tipo de documento.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/historico?tipo={tipo}) - Visualizar histórico de um tipo de documento específico da parte médica.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Visualizar um documento médico específico do paciente.
    * DELETE (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Deletar documento.
    */

    ResponseEntity<Void> uploadMedicalDocument(@RequestPart String patientId, @RequestPart String year, @RequestPart String documentType, @RequestPart MultipartFile file);

    @GetMapping(params = "!tipo")
    ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocuments(@PathVariable String patientId, @PathVariable String year);

    @GetMapping(params = "tipo")
    ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocumentByType(@PathVariable String patientId, @PathVariable Integer year, @RequestParam(required = false) MedcialDocumentType tipoDocumento);

    @GetMapping("/historico")
    ResponseEntity<MedicalDocumentResponseDTO> historyDocumentsByType(@PathVariable String patientId, @RequestParam MedcialDocumentType tipoDocumento);

    @GetMapping("/{documentoId}")
    ResponseEntity<MedicalDocumentResponseDTO> viewPatientDocument(@PathVariable String patientId, @PathVariable String documentoId);

    @PatchMapping("/{documentoId}")
    ResponseEntity<Void> deleteDocument(@PathVariable String documentoId);
}
