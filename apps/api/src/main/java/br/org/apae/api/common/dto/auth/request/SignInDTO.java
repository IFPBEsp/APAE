package br.org.apae.api.common.dto.auth.request;

import br.org.apae.api.common.validations.EmailOrCPF;

public record SignInDTO(
        @EmailOrCPF(checkCpfDigits = false) String username,
        String password) {

}
