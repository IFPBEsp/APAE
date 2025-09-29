package br.org.apae.api.paciente.dto.update;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record UpdatePatientDTO(
        @NotBlank(message = "O campo nome completo é obrigatório.") String fullName,

        String birthplace,

        @NotNull(message = "O campo data de nascimento é obrigatório.") @Past(message = "A data de nascimento deve ser no passado.") LocalDate birthDate,

        String contact,
        String birthCertificateNumber,
        String registryOffice,
        String fls,
        String livro,
        String rg,
        LocalDate issueDate,
        String issuingAgency,
        String cpf,
        String cns,
        String nis,

        @NotNull(message = "O campo data de cadastro é obrigatório.") LocalDate registrationDate,

        String allergies,

        @NotNull(message = "O campo 'é aluno' é obrigatório.") Boolean isStudent,

        @NotNull(message = "O campo endereço é obrigatório.") @Valid UpdateAddressDTO address,

        @NotNull(message = "O campo responsável é obrigatório.") @Valid UpdateGuardianDTO guardian,

        @NotEmpty(message = "A lista de pais/responsáveis não pode estar vazia.") @Valid List<UpdateParentDTO> parents) {
}
