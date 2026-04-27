package br.org.apae.api.patient.application.interfaces;

import java.util.List;
import java.util.UUID;
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
    public List<ReportResponseDTO> getReportsByPatientId(UUID alunoId) {
        return reportViewRepository.findByAlunoId(alunoId).stream()
        .map(view -> new ReportResponseDTO(
            view.getId(),
            view.getAlunoId(),
            view.getAlunoNome(),
            view.getProfessorId(),
            view.getProfessorNome(),
            view.getTurmaId(),
            view.getTurmaNome(),
            view.getAtividades(),
            view.getHabilidades(),
            view.getEstrategias(),
            view.getRecursos(),
            view.getCreatedAt()
        ))
        .collect(Collectors.toList());
    }

    public List<AssessmentResponseDTO> getAssessmentByPatientId(UUID alunoId) {
        return assessmentViewRepository.findByAlunoId(alunoId).stream()
         .map(view -> new AssessmentResponseDTO(
            view.getId(),
            view.getAlunoId(),
            view.getAlunoNome(),
            view.getProfessorId(),
            view.getProfessorNome(),
            view.getDescricao(),
            view.getDataAvaliacao()
        ))
        .collect(Collectors.toList());
    }

}