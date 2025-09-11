package co.com.pragma.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppMessages {
    VALID_TYPE_LOAN_PERSONAL("PERSONAL"),
    VALID_TYPE_LOAN_MORTGAGE("MORTGAGE"),

    //USER
    USER_CREATED("Usuario registrado exitosamente"),
    USER_ALREADY_EXISTS("El usuario ya está registrado con este correo y/o documento"),
    USER_ERROR_SAVING("Error interno al registrar usuario"),
    USER_NOT_FOUND("Usuario no encontrado"),
    USER_FOUND("Usuario encontrado"),
    USER_NOT_EXIST("El usuario no existe"),

    //LOAN
    LOAN_CREATED("Solicitud registrada correctamente"),
    LOAN_ALREADY_EXISTS("Ya existe una solicitud en proceso para este cliente"),
    LOAN_APPLICATION_IN_PROCESS("El cliente ya tiene una solicitud en proceso"),
    LOAN_DUPLICATE_APPLICATION("Ya existe una solicitud duplicada para este cliente"),
    LOAN_INTERNAL_ERROR("Error interno al registrar solicitud"),
    LOAN_TYPE_INVALID("Tipo de préstamo inválido"),

    //COMMON
    VALIDATION_MESSAGE("Error de validación en los datos de entrada."),
    VALIDATION_ERROR("Errores de validación"),
    INTERNAL_ERROR_MESSAGE("Error interno del servidor"),
    CLASS_SHOULD_NOT_BE_INSTANTIATED("La clase de utilidad no debe ser instanciada"),

    //AUTH
    INVALID_CREDENTIALS("Credenciales inválidas"),
    AUTHENTICATION_ERROR("Error durante la autenticación"),;

    private final String message;
}
