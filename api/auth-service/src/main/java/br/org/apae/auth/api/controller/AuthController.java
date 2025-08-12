package br.org.apae.auth.api.controller;

import br.org.apae.auth.api.dto.LoginRequestDTO;
import br.org.apae.auth.api.dto.SignUpRequestDTO;
import br.org.apae.auth.api.dto.TokenResponseDTO;
import br.org.apae.auth.application.service.interfaces.IKeycloakUserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IKeycloakUserRegistrationService keycloakUserRegistrationService;

    @Autowired
    public AuthController(@Qualifier("keycloakUserRegistrationService") IKeycloakUserRegistrationService keycloakUserRegistrationService) {
        this.keycloakUserRegistrationService = keycloakUserRegistrationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) {
        TokenResponseDTO obj = keycloakUserRegistrationService.login(loginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(
        @RequestBody SignUpRequestDTO signUpRequestDto,
        @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = authorizationHeader.replace("Bearer ", "");

        keycloakUserRegistrationService.registerUser(
            signUpRequestDto,
            token
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
