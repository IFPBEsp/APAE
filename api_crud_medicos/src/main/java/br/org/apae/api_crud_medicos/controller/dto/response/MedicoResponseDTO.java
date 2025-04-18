package br.org.apae.api_crud_medicos.controller.dto.response;

import java.util.List;

public record MedicoResponseDTO(
        Long id,
        String nome,
        String especialidade,
        String contato,
        String crm,
        List<String> laudosIds
) {
}
