package br.org.apae.api.common.dto.patient.request.vaccine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

<<<<<<< HEAD:apps/api/src/main/java/br/org/apae/api/common/dto/patient/request/vaccine/VaccineNameDTO.java
public record VaccineNameDTO(

        @NotBlank(message = "O nome da vacina é obrigatório.") @Size(min = 2, max = 100, message = "O nome da vacina deve ter entre 2 e 100 caracteres.") String name

) {
}
=======
public record CreateVaccineDTO(
             @NotBlank(message = "O nome da vacina é obrigatório.") 
             @Size(min = 2, max = 100, message = "O nome da vacina deve ter entre 2 e 100 caracteres.") 
             String name) {
}
>>>>>>> ea1a7055 (feat(vaccines): refatorar os formulários de criação e edição de vacinas):apps/api/src/main/java/br/org/apae/api/common/dto/patient/request/vaccine/CreateVaccineDTO.java
