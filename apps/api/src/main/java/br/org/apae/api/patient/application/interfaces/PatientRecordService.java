package br.org.apae.api.patient.application.interfaces;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.org.apae.api.common.dto.assessment.AssessmentResponseDTO;
import br.org.apae.api.common.dto.report.ReportResponseDTO;
import br.org.apae.api.patient.domain.repository.AssessmentViewRepository;
import br.org.apae.api.patient.domain.repository.ReportViewRepository;

@Service
public class PatientRecordService {

    private final ReportViewRepository reportViewRepository;
    private final AssessmentViewRepository assessmentViewRepository;

    public PatientRecordService(ReportViewRepository reportViewRepository, AssessmentViewRepository assessmentViewRepository){
        this.reportViewRepository = reportViewRepository;
        this.assessmentViewRepository = assessmentViewRepository;
    }
    public List<ReportResponseDTO> getReportsByPatientId(Long patientId) {
        return reportViewRepository.findByPacienteId(patientId).stream()
        .map(view -> new ReportResponseDTO(
            view.getId(),
            view.getCreatedAt(),
            view.getHabilidades(),
            view.getEstrategias(),
            view.getRecursos(),
            view.getProfessorNome(),
            view.getTurmaDescricao()
        ))
        .collect(Collectors.toList());
    }

    public List<AssessmentResponseDTO> getAssessmentByPatientId(Long patientId) {
        return assessmentViewRepository.findByPacienteId(patientId).stream()
         .map(view -> new AssessmentResponseDTO(
            view.getId(),
            view.getDescricao(),
            view.getDataAvaliacao(),
            view.getPacienteId(),
            view.getProfessorNome()
        ))
        .collect(Collectors.toList());
    }
}