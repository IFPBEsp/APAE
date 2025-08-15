package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;
import br.org.apae.profissional_da_saude.domain.repository.HistoricoConsultaRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.HistoricoConsultaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaHistoricoConsultaRepository implements HistoricoConsultaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public HistoricoConsulta salvar(HistoricoConsulta historico) {
        HistoricoConsultaEntity entity = HistoricoConsultaEntity.fromDomain(historico);
        if (historico.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }
        return entity.toDomain();
    }

    @Override
    public List<HistoricoConsulta> listarTodos() {
        return entityManager.createQuery("SELECT h FROM HistoricoConsultaEntity h", HistoricoConsultaEntity.class)
                .getResultList()
                .stream()
                .map(HistoricoConsultaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<HistoricoConsulta> buscarPorId(Long id) {
        HistoricoConsultaEntity entity = entityManager.find(HistoricoConsultaEntity.class, id);
        return Optional.ofNullable(entity).map(HistoricoConsultaEntity::toDomain);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        HistoricoConsultaEntity entity = entityManager.find(HistoricoConsultaEntity.class, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public boolean existePorAgendamentoEData(Long idAgendamento, LocalDate dataConsulta) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(h) FROM HistoricoConsultaEntity h WHERE h.idAgendamento = :idAgendamento AND h.dataConsulta = :dataConsulta",
                        Long.class)
                .setParameter("idAgendamento", idAgendamento)
                .setParameter("dataConsulta", dataConsulta)
                .getSingleResult();
        return count > 0;
    }
}