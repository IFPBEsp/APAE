package br.org.apae.api.patient.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.patient.domain.model.ReportView;

@Repository
public interface ReportViewRepository extends JpaRepository<ReportView, UUID> {
    List<ReportView> findByAlunoId(UUID alunoId);
}