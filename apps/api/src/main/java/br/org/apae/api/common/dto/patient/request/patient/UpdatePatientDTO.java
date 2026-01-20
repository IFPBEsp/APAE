package br.org.apae.api.common.dto.patient.request.patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record UpdatePatientDTO(

        @NotBlank(message = "O nome não pode estar em branco") String fullName,

        @NotBlank(message = "A naturalidade não pode estar em branco") String nationality,

        @NotNull(message = "A data de nascimento não pode ser nula") @Past(message = "A data de nascimento deve ser uma data no passado") LocalDate birthDate,

        @NotBlank(message = "O contato não pode estar em branco") String contact,

        @NotBlank(message = "O número da certidão de nascimento não pode estar em branco") String birthCertificateNumber,

        @NotBlank(message = "O cartório não pode estar em branco") String registryOffice,

        @NotBlank(message = "O campo 'fls' não pode estar em branco") String fls,

        @NotBlank(message = "O campo 'livro' não pode estar em branco") String book,

        @NotBlank(message = "O RG não pode estar em branco") String rg,

        @NotNull(message = "A data de emissão não pode ser nula") LocalDate issueDate,

        @NotBlank(message = "O órgão emissor não pode estar em branco") String issuingAgency,

        @NotBlank(message = "O CPF não pode estar em branco") String cpf,

        @NotBlank(message = "O CNS não pode estar em branco") String cns,

        @NotBlank(message = "O NIS não pode estar em branco") String nis,

        @NotNull(message = "A data de cadastro não pode ser nula") LocalDate registrationDate,

        @NotBlank(message = "As alergias não podem estar em branco") String allergies,

        @NotNull(message = "O campo 'é estudante' não pode ser nulo") boolean isStudent,

        @NotNull(message = "Os dados de endereço são obrigatórios") @Valid CreateAddressDTO address,

        @NotNull(message = "Os dados do responsável são obrigatórios") @Valid UpdateGuardianDTO guardian,

        @NotEmpty(message = "É necessário fornecer os dados de pelo menos um parente ou responsável legal") @Valid List<UpdateParentDTO> parents,

        @NotEmpty(message = "A lista de vacinas não pode estar vazia") Set<CreateVaccineDTO> vaccineNames) {
}