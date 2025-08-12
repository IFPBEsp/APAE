package br.org.apae.profissional_da_saude.infrastructure.persistency.mapper;

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


}
