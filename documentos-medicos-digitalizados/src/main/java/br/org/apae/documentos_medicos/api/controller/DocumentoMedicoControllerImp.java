package br.org.apae.documentos_medicos.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.application.MedicalDocumentService;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;


@RestController
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> anexarDocumentoMedico(@RequestPart String patientId, @RequestPart String year, @RequestPart String documentType, @RequestPart MultipartFile file) {
        var data = new MedicalDocumentUploadDTO(patientId, Integer.parseInt(year), documentType);
        medicalDocumentService.saveFile(data, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Override
    @GetMapping("/{patientId}/documentos/{year}")
    public ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocuments(@PathVariable String patientId, @PathVariable String year) {
        var documents = medicalDocumentService.listMedicalDocument(patientId, Integer.parseInt(year));
        if (documents.urls().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(documents);
    }

    @Override
    @GetMapping(params = "tipo")
    public ResponseEntity<List<MedicalDocumentResponseDTO>> listMedicalDocumentByType(@PathVariable String patientId, MedcialDocumentType tipoDocumento) {
        return null;
    }

    @Override
    public ResponseEntity<List<MedicalDocumentResponseDTO>> historicoTipoDocumento(@PathVariable String patientId, MedcialDocumentType tipoDocumento) {
        return null;
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> visualizarDocumentoMedicosPaciente(@PathVariable String patientId, UUID documentoId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletarDocumento(@PathVariable String documentoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletarDocumento'");
    }




}

