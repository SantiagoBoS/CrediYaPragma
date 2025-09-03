package co.com.pragma.api.exception;

public class AuthUtils {
    public static final String SUCCESS_CODE = "200.01";
    public static final String SUCCESS_MESSAGE = "Login exitoso";

    public static final String CREATE_CODE = "201.01";
    public static final String CREATE_MESSAGE = "Usuario encontrado";

    public static final String VALIDATION_CODE = "400.01";
    public static final String VALIDATION_CODE_GENERAL = "400.02";
    public static final String VALIDATION_MESSAGE = "Error de validación en las credenciales";

    public static final String INVALID_CREDENTIALS = "401.01";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Credenciales incorrectas";

    public static final String CONFLICT_CODE = "409.01";
    public static final String CONFLICT_MESSAGE = "El usuario ya se encuentra registrado";

    public static final String INTERNAL_ERROR_CODE = "500.01";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor.";

    public static final String TEXT_VALID_LOGIN = "Ejemplo Login Correcto";
    public static final String EXAMPLE_VALID_LOGIN = "{ \"code\": \"200.01\", \"message\": \"Login exitoso\", \"data\": { \"token\": \"eyJhbGciOiJIUzUxMiJ9...\" } }";

    public static final String EXAMPLE_VALIDATION_ERROR = "{ \"code\": \"400.01\", \"message\": \"Error de validación\", \"errors\": [{ \"field\": \"email\", \"description\": \"Correo inválido\" }] }";

    public static final String TEXT_ERROR_AUTH = "Ejemplo error de autenticación";
    public static final String EXAMPLE_ERROR_AUTH = "{ \"code\": \"401.01\", \"message\": \"Credenciales inválidas\" }";
}