package br.org.apae.documentos_medicos.api.controller;

import java.io.FileNotFoundException;

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
     * DELETE (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId})- Deletar um documento específico
     
*/

    private MedicalDocumentService medicalDocumentService;

    @Autowired
    public DocumentoMedicoControllerImp(MedicalDocumentService medicalDocumentService) {
        this.medicalDocumentService = medicalDocumentService;
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadMedicalDocument(@RequestPart String patientId, @RequestPart String year, @RequestPart String documentType, @RequestPart MultipartFile file) {
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
    public ResponseEntity<MedicalDocumentResponseDTO> listMedicalDocumentByType(@PathVariable String patientId, @PathVariable Integer year , @PathVariable MedcialDocumentType type) {
        var documents = medicalDocumentService.listMedicalDocumentByType(patientId, year, type);
        if (documents.urls().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(documents);
    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> historyDocumentsByType(@PathVariable String patientId, MedcialDocumentType type) {
        try {
            var documents = medicalDocumentService.historyDocumentByType(patientId, type);

            if (documents.urls().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(documents);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    public ResponseEntity<MedicalDocumentResponseDTO> viewPatientDocument(@PathVariable String patientId, @PathVariable String documentoId) {
        try {
            var documents = medicalDocumentService.viewPatientDocument(patientId, documentoId);

            if (documents.urls().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(documents);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    public ResponseEntity<Void> deleteDocument(String patientId, String documentId) {
        try {
            medicalDocumentService.deleteDocument(patientId, documentId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (e.getMessage().contains("O documento não foi encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

