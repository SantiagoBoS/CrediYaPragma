package co.com.pragma.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppMessages {
    USER_ALREADY_EXISTS("El usuario ya está registrado con este correo y/o documento"),
    ERROR_SAVING_USER("Error interno al registrar usuario"),
    USER_NOT_FOUND("Usuario no encontrado"),
    USER_FOUND("Usuario encontrado"),
    USER_NOT_EXIST("El usuario no existe"),


    APPLICATION_IN_PROCESS("El cliente ya tiene una solicitud en proceso"),
    DUPLICATE_APPLICATION("Ya existe una solicitud duplicada para este cliente"),
    INTERNAL_ERROR("Error interno al registrar solicitud"),
    CLASS_SHOULD_NOT_BE_INSTANTIATED("La clase de utilidad no debe ser instanciada"),
    VALID_TYPE_LOAN_PERSONAL("PERSONAL"),
    VALID_TYPE_LOAN_MORTGAGE("MORTGAGE"),
    VALID_TYPE_LOAN_CAR("CAR"),
    VALIDATION_DUPLICATE("duplicada"),
    VALIDATION_INTERNAL_ERROR("Error interno");

    private final String message;
}
