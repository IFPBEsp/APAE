package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;
import br.org.apae.profissional_da_saude.domain.repository.HistoricoConsultaRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.HistoricoConsultaEntity;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.HistoricoConsultaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HistoricoConsultaRepositoryImpl implements HistoricoConsultaRepository {

    private final HistoricoConsultaJpaRepository jpaRepository;

    @Override
    public HistoricoConsulta salvar(HistoricoConsulta historico) {
        HistoricoConsultaEntity entity = HistoricoConsultaEntity.fromDomain(historico);
        HistoricoConsultaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public List<HistoricoConsulta> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(HistoricoConsultaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<HistoricoConsulta> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(HistoricoConsultaEntity::toDomain);
    }

    @Override
    public void deletar(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorAgendamentoEData(UUID idAgendamento, LocalDate dataConsulta, LocalTime horaConsulta) {
        return jpaRepository.existsByIdAgendamentoAndDataConsultaAndHoraConsulta(idAgendamento, dataConsulta, horaConsulta);
    }
}
