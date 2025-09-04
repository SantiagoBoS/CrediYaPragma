package co.com.pragma.model.user.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppMessages {
    USER_ALREADY_EXISTS("El usuario ya está registrado con este correo y/o documento"),
    ERROR_SAVING_USER("Error interno al registrar usuario"),
    EMAIL_FIELD("email"),
    DOCUMENT_FIELD("document_number"),
    CLASS_SHOULD_NOT_BE_INSTANTIATED("Esta clase no debe ser instanciada"),
    VALIDATOR_HANDLE_FIELD("field"),
    VALIDATOR_HANDLE_MESSAGE("message");

    private final String message;
}
