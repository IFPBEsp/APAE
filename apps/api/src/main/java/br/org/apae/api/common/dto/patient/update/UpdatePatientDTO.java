package br.org.apae.api.common.dto.patient.update;

import java.time.LocalDate;
import java.util.List;

import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record UpdatePatientDTO(
        @NotBlank(message = "O nome completo não pode estar em branco")
        String fullName,

        @NotBlank(message = "A naturalidade não pode estar em branco")
        String nationality,

        @NotNull(message = "A data de nascimento não pode ser nula")
        @Past(message = "A data de nascimento deve ser uma data no passado")
        LocalDate birthDate,
        String contact,
        String birthCertificateNumber,
        String registryOffice,
        String fls,
        String book,
        String rg,
        LocalDate issueDate,
        String issuingAgency,
        String cpf,
        String cns,
        String nis,
        @NotNull(message = "A data de cadastro não pode ser nula")
        LocalDate registrationDate,

        String allergies,
        Boolean isStudent,

        @NotNull(message = "Os dados de endereço são obrigatórios")
        @Valid
        UpdateAddressDTO address,

        @NotNull(message = "Os dados do responsável são obrigatórios")
        @Valid
        UpdateGuardianDTO guardian,

        @NotEmpty(message = "É necessário fornecer os dados de pelo menos um pai ou responsável legal")
        @Valid
        List<UpdateParentDTO> parents
) {
}

