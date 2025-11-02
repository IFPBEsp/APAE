package br.org.apae.api.appointment.application.interfaces;

import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AbsenceApplicationService {
    /**
     * Registra uma nova falta vinculada a um agendamento gerado existente.
     * @param dto DTO com os dados da falta.
     * @return O DTO da falta registrada.
     */
    AbsenceResponseDTO register(CreateAbsenceDTO dto);

    /**
     * Busca faltas com filtros dinâmicos e paginação.
     * @param generatedId Filtro opcional por ID do Agendamento Gerado.
     * @param patientId Filtro opcional por ID do Paciente.
     * @param professionalId Filtro opcional por ID do Profissional.
     * @param pageable Informações de paginação.
     * @return Página de DTOs de Faltas.
     */
    Page<AbsenceResponseDTO> findAllByFilters(
            UUID generatedId,
            UUID patientId,
            UUID professionalId,
            Pageable pageable
    );
}