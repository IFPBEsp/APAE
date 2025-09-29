package br.org.apae.api.paciente.dto.create;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;


public record CreatePatientDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String fullName,

        @NotBlank(message = "A naturalidade não pode estar em branco")
        String nationality,

        @NotNull(message = "A data de nascimento não pode ser nula")
        @Past(message = "A data de nascimento deve ser uma data no passado")
        LocalDate dateOfBirth,

        @NotBlank(message = "O contato não pode estar em branco")
        String contact,

        String birthRecord,
        String fls,
        String book,
        LocalDate issueDate,
        String issuingAuthority,
        String cpf,
        String cns,
        String allergies,
        Boolean isStudent,

        @NotNull(message = "Os dados de endereço são obrigatórios")
        @Valid
        CreateAddressDTO address,

        @NotNull(message = "Os dados do responsável são obrigatórios")
        @Valid
        CreateGuardianDTO guardian,

        @NotEmpty(message = "É necessário fornecer os dados de pelo menos um pai ou responsável legal")
        @Valid
        List<CreateParentDTO> parents
) {
}
