package br.org.apae.documentos_medicos.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentRequestDTO;
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
    public ResponseEntity<MedicalDocumentResponseDTO> anexarDocumentoMedico(UUID pacienteId,
            @Valid MedicalDocumentRequestDTO documentoAnexado, MultipartFile arquivo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'anexarDocumentoMedico'");
    }

    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> listarDocumentosMedicosPaciente(UUID pacienteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarDocumentosMedicosPaciente'");
    }

    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> filtrarPorTipoDocumento(UUID pacienteId,
            MedcialDocumentType tipoDocumento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filtrarPorTipoDocumento'");
    }

    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> historicoTipoDocumento(UUID pacienteId,
            MedcialDocumentType tipoDocumento) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'historicoTipoDocumento'");
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> visualizarDocumentoMedicosPaciente(UUID pacienteID,
            UUID documentoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visualizarDocumentosMedicosPaciente'");
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> atualizarDocumento(UUID pacienteId, UUID documentoId, MedicalDocumentRequestDTO documentoAtualizado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarDocumento'");
    }

    @Override
    public ResponseEntity<Void> desativarDocumento(UUID pacienteId, UUID documentoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desativarDocumento'");
    }


}

