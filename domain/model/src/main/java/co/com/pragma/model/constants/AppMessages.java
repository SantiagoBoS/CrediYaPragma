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
    ROLE_NOT_FOUND("Rol no encontrado"),

    //LOAN
    LOAN_CREATED("Solicitud registrada correctamente"),
    LOAN_DUPLICATE_APPLICATION("Ya existe una solicitud duplicada para este cliente"),
    LOAN_INTERNAL_ERROR("Error interno al registrar solicitud"),
    LOAN_TYPE_INVALID("Tipo de préstamo inválido"),
    LOAN_CANNOT_BE_CREATED_FOR_ANOTHER_USER("No puedes crear un préstamo para otro usuario"),
    LOAN_PUBLIC_ID_REQUIRED("El ID público de la solicitud es requerido"),
    LOAN_STATUS_REQUIRED("El estado de la solicitud es requerido"),
    LOAN_ADVISOR_REQUIRED("El ID del asesor es requerido"),
    LOAN_STATUS_INVALID("Estado inválido. Solo se permite APPROVED o REJECTED"),
    LOAN_ALREADY_PROCESSED("La solicitud ya ha sido procesada"),
    LOAN_UPDATE_ERROR("Error al actualizar la solicitud"),
    LOAN_NOT_FOUND("Solicitud no encontrada"),
    LOAN_STATUS_UPDATED("Estado de solicitud actualizado exitosamente"),

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
