package br.org.apae.documentos_medicos.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.application.MedicalDocumentService;

@RestController
@RequestMapping("/api/v1/documentos-medicos")
public class MedicalDocumentController {
    
    private MedicalDocumentService medicalDocumentService;

    @Autowired
    public MedicalDocumentController(MedicalDocumentService medicalDocumentService) {
        this.medicalDocumentService = medicalDocumentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadMedicalDocument(@RequestPart String patientId, @RequestPart String year, @RequestPart String documentType, @RequestPart MultipartFile file) {
        var data = new MedicalDocumentUploadDTO(patientId, Integer.parseInt(year), documentType);
        medicalDocumentService.saveFile(data, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
