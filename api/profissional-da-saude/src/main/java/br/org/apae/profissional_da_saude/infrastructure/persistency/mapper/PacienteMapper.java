package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

import br.org.apae.profissional_da_saude.api.dto.PacienteCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.PacienteResposeDTO;
import br.org.apae.profissional_da_saude.domain.model.Paciente;
import br.org.apae.profissional_da_saude.infrastructure.entity.PacienteEntity;

public final class PacienteMapper {

    public PacienteMapper() {}

    public static PacienteEntity toEntity(Paciente paciente) {
        return PacienteEntity.builder()
            .id(paciente.getId())
            .cep(paciente.getCep())
            .email(paciente.getEmail())
            .cpf(paciente.getCpf())
            .rg(paciente.getRg())
            .nome(paciente.getNome())
            .bairro(paciente.getBairro())
            .cidade(paciente.getCidade())
            .dateNascimento(paciente.getDateNascimento())
            .endereco(paciente.getEndereco())
            .estado(paciente.getEstado())
            .telefone(paciente.getTelefone())
            .dataCriacao(paciente.getDataCriacao())
            .build();
    }

    public static Paciente toModel(PacienteEntity entity) {
        return new Paciente(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getTelefone(),
            entity.getDateNascimento(),
            entity.getCpf(),
            entity.getRg(),
            entity.getEndereco(),
            entity.getBairro(),
            entity.getCidade(),
            entity.getEstado(),
            entity.getCep(),
            entity.getDataCriacao()
        );
    }

    public static PacienteResposeDTO toResponseDTO(Paciente paciente) {
        return PacienteResposeDTO.builder()
                .id(paciente.getId())
                .bairro(paciente.getBairro())
                .cep(paciente.getCep())
                .cidade(paciente.getCidade())
                .cpf(paciente.getCpf())
                .nome(paciente.getNome())
                .estado(paciente.getEstado())
                .telefone(paciente.getTelefone())
                .email(paciente.getEmail())
                .dateNascimento(paciente.getDateNascimento())
                .endereco(paciente.getEndereco())
                .rg(paciente.getRg())
                .dataCriacao(paciente.getDataCriacao())
                .build();
    }

    public static Paciente toDomain(PacienteCreateDTO dto) {
        return new Paciente(
            dto.getNome(),
            dto.getEmail(),
            dto.getTelefone(),
            dto.getDateNascimento(),
            dto.getCpf(),
            dto.getRg(),
            dto.getEndereco(),
            dto.getBairro(),
            dto.getCidade(),
            dto.getEstado(),
            dto.getCep()
        );
    }
}
