package co.com.pragma.api.exception;

public class UserUtils {
    public static final String CREATE_CODE = "201.01";
    public static final String CREATE_MESSAGE = "Usuario registrado exitosamente";

    public static final String VALIDATION_CODE = "400.01";
    public static final String VALIDATION_CODE_GENERAL = "400.02";
    public static final String VALIDATION_MESSAGE = "Error de validación en los datos de entrada.";

    public static final String CONFLICT_CODE = "409.01";
    public static final String CONFLICT_MESSAGE = "El usuario ya se encuentra registrado.";

    public static final String INTERNAL_ERROR_CODE = "500.01";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor.";


    public static final String TEXT_VALID_USER = "Usuario válido";
    public static final String EXAMPLE_VALID_USER = "{ \"documentNumber\": \"123456789\", \"name\": \"Santiago\", \"lastName\": \"Borrero\", \"birthDate\": \"1995-05-10\", \"address\": \"Calle 123\", \"phone\": \"3001234567\", \"email\": \"santi@correo.com\", \"baseSalary\": \"3500000\" }";

    public static final String TEXT_VALIDATION_CORRECT = "Respuesta exitosa";
    public static final String EXAMPLE_VALIDATION_CORRECT = "{ \"code\": \"201.01\", \"message\": \"Usuario registrado exitosamente\", \"data\": { \"name\": \"Santiago\", \"lastName\": \"Borrero\", \"birthDate\": \"1995-05-10\", \"address\": \"Calle 123\", \"phone\": \"3001234567\", \"email\": \"santi@correo.com\", \"baseSalary\": 3500000 } }";

    public static final String TEXT_ERROR_VALIDATION = "Error de validación";
    public static final String EXAMPLE_ERROR_VALIDATION = "{ \"code\": \"400.01\", \"message\": \"Error de validación en los datos de entrada.\", \"errors\": [ \"El campo 'email' es obligatorio.\", \"El campo 'documentNumber' es obligatorio.\" ] }";

    public static final String TEXT_VALIDATION_DUPLICATE = "Conflicto por duplicado";
    public static final String EXAMPLE_VALIDATION_DUPLICATE = "{ \"code\": \"409.01\", \"message\": \"El correo ya se encuentra registrado\" }";

    public static final String TEXT_INTERNAL_ERROR = "Error interno del servidor";
    public static final String EXAMPLE_INTERNAL_ERROR = "{ \"code\": \"500.01\", \"message\": \"Error interno del servidor.\" }";
}
