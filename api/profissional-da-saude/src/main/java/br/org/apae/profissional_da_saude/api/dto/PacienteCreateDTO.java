package br.org.apae.profissional_da_saude.api.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteCreateDTO {

    @NotNull(message = "O nome do paciente é obrigatório.")
    private String nome;

    @NotNull(message = "O e-mail do paciente é obrigatório.")
    private String email;

    @NotNull(message = "O telefone do paciente é obrigatório.")
    private String telefone;

    @NotNull(message = "A data de nascimento do paciente é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNascimento;

    @NotNull(message = "O CPF do paciente é obrigatório.")
    private String cpf;

    @NotNull(message = "O RG do paciente é obrigatório.")
    private String rg;

    @NotNull(message = "O endereço do paciente é obrigatório.")
    private String endereco;

    @NotNull(message = "O bairro do paciente é obrigatório.")
    private String bairro;

    @NotNull(message = "A cidade do paciente é obrigatória.")
    private String cidade;

    @NotNull(message = "O estado do paciente é obrigatório.")
    private String estado;

    @NotNull(message = "O CEP do paciente é obrigatório.")
    private String cep;
}