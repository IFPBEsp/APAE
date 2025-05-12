package br.org.apae.documentos_medicos.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import org.springframework.web.multipart.MultipartFile;


import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

@RequestMapping("/api/v2/documentos-medicos")
public interface BaseController {

    /*
    * POST (/api/pacientes/{pacienteId}/documentos-medicos) - Anexar documentos médicos a um paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos) - Listar todos os documentos médicos associados a 1 paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos?tipo={tipo}) - Filtrar por tipo de documento.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/historico?tipo={tipo}) - Visualizar histórico de um tipo de documento específico da parte médica.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Visualizar um documento médico específico do paciente.
    * DELETE (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Deletar documento.
    */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> anexarDocumentoMedico(@RequestPart String patientId, @RequestPart String year, @RequestPart String documentType, @RequestPart MultipartFile file);

    @GetMapping(params = "!tipo")
    ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocuments(@PathVariable String patientId, @PathVariable String year);

    @GetMapping(params = "tipo")
    ResponseEntity<List<MedicalDocumentResponseDTO>> listMedicalDocumentByType(@PathVariable String patientId, @RequestParam(required = false) MedcialDocumentType tipoDocumento);

    @GetMapping("/historico")
    ResponseEntity<List<MedicalDocumentResponseDTO>> historicoTipoDocumento(@PathVariable String patientId, @RequestParam MedcialDocumentType tipoDocumento);

    @GetMapping("/{documentoId}")
    ResponseEntity<MedicalDocumentResponseDTO> visualizarDocumentoMedicosPaciente(@PathVariable String patientId, @PathVariable UUID documentoId);

    @PatchMapping("/{documentoId}")
    ResponseEntity<Void> deletarDocumento(@PathVariable String documentoId);
}
