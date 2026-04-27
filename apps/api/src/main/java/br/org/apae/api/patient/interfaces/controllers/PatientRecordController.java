package br.org.apae.api.patient.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.common.dto.assessment.AssessmentResponseDTO;
import br.org.apae.api.common.dto.report.ReportResponseDTO;
import br.org.apae.api.patient.application.interfaces.PatientRecordService;
 

@RestController
@RequestMapping("/patients")
public class PatientRecordController {
    private final PatientRecordService patientRecordService;

    public PatientRecordController(PatientRecordService patientRecordService){
        this.patientRecordService = patientRecordService;
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<List<ReportResponseDTO>> getReports(@PathVariable UUID id){
        return ResponseEntity.ok(patientRecordService.getReportsByPatientId(id));
    }
    
    @GetMapping("/{id}/assessments")
    public ResponseEntity<List<AssessmentResponseDTO>> getAssessment(@PathVariable UUID id){
        return ResponseEntity.ok(patientRecordService.getAssessmentByPatientId(id));
    }
}