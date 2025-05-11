package br.org.apae.documentos_medicos.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentRequestDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import jakarta.validation.Valid;

@RequestMapping("/api/pacientes/{pacienteId}/documentos-medicos")
public interface BaseController {

    /*
    * POST (/api/pacientes/{pacienteId}/documentos-medicos) - Anexar documentos médicos a um paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos) - Listar todos os documentos médicos associados a 1 paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos?tipo={tipo}) - Filtrar por tipo de documento.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/historico?tipo={tipo}) - Visualizar histórico de um tipo de documento específico da parte médica.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Visualizar um documento médico específico do paciente.
    * PUT (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Atualizar documento.
    * PATCH (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Desativar documento.
    */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MedicalDocumentResponseDTO> anexarDocumentoMedico(@PathVariable UUID pacienteId, @Valid @RequestPart MedicalDocumentRequestDTO documentoAnexado, @RequestPart MultipartFile arquivo);

    @GetMapping(params = "!tipo")
    ResponseEntity<List<MedicalDocumentResponseDTO>> listarDocumentosMedicosPaciente(@PathVariable UUID pacienteId);

    @GetMapping(params = "tipo")
    ResponseEntity<List<MedicalDocumentResponseDTO>> filtrarPorTipoDocumento(@PathVariable UUID pacienteId, @RequestParam(required = false) MedcialDocumentType tipoDocumento);

    @GetMapping("/historico")
    ResponseEntity<List<MedicalDocumentResponseDTO>> historicoTipoDocumento(@PathVariable UUID pacienteId, @RequestParam MedcialDocumentType tipoDocumento);

    @GetMapping("/{documentoId}")
    ResponseEntity<MedicalDocumentResponseDTO> visualizarDocumentoMedicosPaciente(@PathVariable UUID pacienteId, @PathVariable UUID documentoId);

    @PutMapping("/{documentoId}")
    ResponseEntity<MedicalDocumentResponseDTO> atualizarDocumento(@PathVariable UUID pacienteId, @PathVariable UUID documentoId, @Valid @RequestPart MedicalDocumentRequestDTO documentoAtualizado);

    @PatchMapping("/{documentoId}")
    ResponseEntity<Void> desativarDocumento(@PathVariable UUID pacienteId, @PathVariable UUID documentoId);
}
