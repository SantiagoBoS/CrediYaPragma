package co.com.pragma.api.exception;

public class ErrorCatalog {
    public static final String CREATE_CODE = "201.01";
    public static final String CREATE_MESSAGE = "Usuario registrado exitosamente";

    public static final String VALIDATION_CODE = "400.01";
    public static final String VALIDATION_CODE_GENERAL = "400.02";
    public static final String VALIDATION_MESSAGE = "Error de validación en los datos de entrada.";

    public static final String CONFLICT_CODE = "409.01";
    public static final String CONFLICT_MESSAGE = "El usuario ya se encuentra registrado.";

    public static final String INTERNAL_ERROR_CODE = "500.01";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor.";
}
