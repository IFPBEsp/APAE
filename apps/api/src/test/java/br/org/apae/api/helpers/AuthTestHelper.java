package br.org.apae.api.helpers;

import static org.mockito.Mockito.when;

import br.org.apae.api.auth.application.internal.UserService;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.auth.infrastructure.security.JwtProvider;

public class AuthTestHelper {

    public static final String TOKEN = "fake-jwt-token";
    public static final String USERNAME = "teste@apae.org.br";

    public static User mockAuthenticatedUser(
            JwtProvider jwtProvider,
            UserService userService
    ) {
        User user = new User(
                USERNAME,
                "encoded-password",
                "12345678900",
                "Usuário Teste",
                UserRole.ADMIN
        );

        when(jwtProvider.validateToken(TOKEN)).thenReturn(USERNAME);
        when(userService.findUserByUsername(USERNAME)).thenReturn(user);

        return user;
    }

    public static String bearerToken() {
        return "Bearer " + TOKEN;
    }
}