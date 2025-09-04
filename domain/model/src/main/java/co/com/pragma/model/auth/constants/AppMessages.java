package co.com.pragma.model.auth.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppMessages {
    USER_NOT_FOUND("Usuario no encontrado"),
    INVALID_CREDENTIALS("Credenciales inválidas"),
    AUTHENTICATION_ERROR("Error durante la autenticación");

    private final String message;
}
