package co.com.pragma.api.exception;

public class ErrorCatalog {

    // Errores de validación
    public static final String VALIDATION_CODE = "400.01";
    public static final String VALIDATION_MESSAGE = "Error de validación en los datos de entrada.";

    // Conflictos (ej: usuario ya existe)
    public static final String CONFLICT_CODE = "409.01";
    public static final String CONFLICT_MESSAGE = "El usuario ya se encuentra registrado.";

    // Error interno del servidor
    public static final String INTERNAL_ERROR_CODE = "500.01";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor.";
}
