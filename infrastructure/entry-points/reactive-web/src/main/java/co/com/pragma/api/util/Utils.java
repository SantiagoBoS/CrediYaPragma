package co.com.pragma.api.util;

public class Utils {
    public static final String SUCCESS_CODE = "200.00";
    public static final String SUCCESS_MESSAGE = "OK";

    public static final String CREATE_CODE = "201.01";

    public static final String VALIDATION_CODE = "400.01";
    public static final String VALIDATION_CODE_GENERAL = "400.02";
    public static final String VALIDATION_MESSAGE = "Error de validación en los datos de entrada.";
    public static final String VALIDATION_ERROR = "Errores de validación";

    public static final String CONFLICT_CODE = "409.01";

    public static final String INTERNAL_ERROR_CODE = "500.01";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor";


    public static final String USER_CREATE_MESSAGE = "Usuario registrado exitosamente";
    public static final String USER_VALIDATION_ERROR_USER_EXISTS = "Usuario ya existe";
    public static final String USER_CONFLICT_MESSAGE = "El usuario ya se encuentra registrado.";

    public static final String USER_TEXT_VALID_USER = "Usuario válido";
    public static final String USER_EXAMPLE_VALID_USER = "{ \"documentNumber\": \"123456789\", \"name\": \"Santiago\", \"lastName\": \"Borrero\", \"birthDate\": \"1995-05-10\", \"address\": \"Calle 123\", \"phone\": \"3001234567\", \"email\": \"santi@correo.com\", \"baseSalary\": \"3500000\" }";
    public static final String USER_TEXT_VALIDATION_CORRECT = "Respuesta exitosa";
    public static final String USER_EXAMPLE_VALIDATION_CORRECT = "{ \"code\": \"201.01\", \"message\": \"Usuario registrado exitosamente\", \"data\": { \"name\": \"Santiago\", \"lastName\": \"Borrero\", \"birthDate\": \"1995-05-10\", \"address\": \"Calle 123\", \"phone\": \"3001234567\", \"email\": \"santi@correo.com\", \"baseSalary\": 3500000 } }";
    public static final String USER_TEXT_ERROR_VALIDATION = "Error de validación";
    public static final String USER_EXAMPLE_ERROR_VALIDATION = "{ \"code\": \"400.01\", \"message\": \"Error de validación en los datos de entrada.\", \"errors\": [ \"El campo 'email' es obligatorio.\", \"El campo 'documentNumber' es obligatorio.\" ] }";
    public static final String USER_TEXT_VALIDATION_DUPLICATE = "Conflicto por duplicado";
    public static final String USER_EXAMPLE_VALIDATION_DUPLICATE = "{ \"code\": \"409.01\", \"message\": \"El correo ya se encuentra registrado\" }";
    public static final String USER_TEXT_INTERNAL_ERROR = "Error interno del servidor";
    public static final String USER_EXAMPLE_INTERNAL_ERROR = "{ \"code\": \"500.01\", \"message\": \"Error interno del servidor\" }";

    public static final String USER_PATH_API_USERS = "/api/v1/usuarios";
    public static final String USER_PATH_OPERATION_ID = "registerUser";
    public static final String USER_PATH_OPERATION_SUMMARY = "Registrar un nuevo usuario";
    public static final String USER_PATH_OPERATION_DESCRIPTION = "Permite registrar un nuevo solicitante proporcionando sus datos personales";
    public static final String USER_PATH_REQUEST_BODY_DESCRIPTION = "Datos del nuevo usuario";



    public static final String LOAN_CREATE_MESSAGE = "Solicitud registrada correctamente";
    public static final String LOAN_CONFLICT_MESSAGE = "Ya existe una solicitud en proceso para este cliente";

    public static final String LOAN_TEXT_VALID_LOAN = "Solicitud válida";
    public static final String LOAN_EXAMPLE_VALID_LOAN = "{ \"clientDocument\": \"123456789\", \"amount\": 5000000, \"term\": 24, \"loanType\": \"PERSONAL\" }";
    public static final String LOAN_TEXT_VALIDATION_CORRECT = "Solicitud creada";
    public static final String LOAN_EXAMPLE_VALIDATION_CORRECT = "{ \"code\": \"201.01\", \"message\": \"Solicitud registrada exitosamente\", \"data\": { \"clientDocument\": \"123456789\", \"amount\": 5000000, \"term\": 24, \"loanType\": \"PERSONAL\", \"status\": \"PENDING_REVIEW\" } }";
    public static final String LOAN_TEXT_ERROR_VALIDATION =  "Error de validación";
    public static final String LOAN_EXAMPLE_ERROR_VALIDATION = "{ \"code\": \"400.01\", \"message\": \"Error de validación en los datos de entrada.\", \"errors\": [ \"El campo 'amount' debe ser mayor a 0\", \"El campo 'term' es obligatorio\" ] }";
    public static final String LOAN_EXAMPLE_VALIDATION_DUPLICATE = "{ \"code\": \"409.01\", \"message\": \"El cliente ya tiene una solicitud en proceso\" }";
    public static final String LOAN_EXAMPLE_INTERNAL_ERROR = "{ \"code\": \"500.01\", \"message\": \"Ocurrió un error inesperado. Intente nuevamente más tarde.\" }";

    public static final String LOAN_ROUTER_BASE_PATH = "/api/v1/solicitud";
    public static final String LOAN_ROUTER_OPERATION_ID = "createLoan";
    public static final String LOAN_ROUTER_SUMMARY = "Registrar una nueva solicitud de préstamo";
    public static final String LOAN_ROUTER_DESCRIPTION = "Permite registrar una solicitud de préstamo proporcionando documento del cliente, monto, plazo y tipo de préstamo";
    public static final String LOAN_ROUTER_REQUEST_DESCRIPTION = "Datos de la nueva solicitud de préstamo";
}
