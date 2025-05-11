package br.org.apae.documentos_medicos.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentRequestDTO;
import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.application.MedicalDocumentService;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/documentos-medicos")
public class DocumentoMedicoControllerImp implements BaseController {

/*
     * POST (/api/pacientes/{pacienteId}/documentos-medicos) - Anexar documentos médicos a um paciente.
     * GET (/api/pacientes/{pacienteId}/documentos-medicos) - Listar todos os documentos associados a 1 paciente.
     * GET (/api/pacientes/{pacienteId}/documentos-medicos?tipo=medico) - Filtrar por tipo de documento?
     * GET (/api/pacientes/{pacienteId}/documentos-medicos/historico?tipo={tipo})- Visualizar histórico de um tipo de documento especifico da parte médica(Seja exames, laúdos ou encaminhamentos).
     * GET (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Visualizar todos os documentos médicos do paciente;
     * PUT (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Atualizar documento
     * DELETE (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId})- Desativar documento
     
*/

    private MedicalDocumentService medicalDocumentService;

    @Autowired
    public DocumentoMedicoControllerImp(MedicalDocumentService medicalDocumentService) {
        this.medicalDocumentService = medicalDocumentService;
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> anexarDocumentoMedico( UUID pacienteId, 
        @Valid MedicalDocumentRequestDTO documentoAnexado, 
        MultipartFile arquivo) {
        MedicalDocumentUploadDTO dto = new MedicalDocumentUploadDTO(
            pacienteId.toString(),  
            documentoAnexado.getDataReferencia().getYear(),
            documentoAnexado.getTipo().name());

        medicalDocumentService.saveFile(dto, arquivo);

        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> listarDocumentosMedicosPaciente(UUID pacienteId) {
        MedicalDocumentResponseDTO response = medicalDocumentService.listMedicalDocument(pacienteId.toString(), LocalDate.now().getYear());
        return ResponseEntity.ok(List.of(response));
    }

    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> filtrarPorTipoDocumento(UUID pacienteId,
            MedcialDocumentType tipoDocumento) {
         MedicalDocumentResponseDTO response = medicalDocumentService.listMedicalDocumentByType(pacienteId.toString(), LocalDate.now().getYear(), tipoDocumento);
        return ResponseEntity.ok(List.of(response));
    }

    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> historicoTipoDocumento(UUID pacienteId,
            MedcialDocumentType tipoDocumento) {
        MedicalDocumentResponseDTO response = medicalDocumentService.historicoTipoDocumento(pacienteId.toString(), tipoDocumento);
        return ResponseEntity.ok(List.of(response));
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> visualizarDocumentoMedicosPaciente(UUID pacienteId,
            UUID documentoId) {
        return ResponseEntity.ok(medicalDocumentService.visualizarDocumentosMedicosPaciente(pacienteId, documentoId));
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> atualizarDocumento(UUID pacienteId, UUID documentoId, MedicalDocumentRequestDTO documentoAtualizado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarDocumento'");
    }

    @Override
    public ResponseEntity<Void> desativarDocumento(UUID pacienteId, UUID documentoId) {
        medicalDocumentService.desativarDocumento(pacienteId, documentoId);
        return ResponseEntity.noContent().build();
    }


}

