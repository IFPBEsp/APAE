package br.org.apae.api_crud_medicos.controller.dto.request;

import java.util.List;

public record MedicoRequestDTO(
        String nome,
        String especialidade,
        String contato,
        String crm,
        List<String> laudosIds
) {
}
