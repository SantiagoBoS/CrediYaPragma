package co.com.pragma.model.constants;

public final class SwaggerConstants {
    private SwaggerConstants() {
        throw new UnsupportedOperationException(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    //BASE CODES
    public static final String CREATE_CODE = "201";
    public static final String VALIDATION_CODE = "400";
    public static final String UNAUTHORIZED_CODE = "401";
    public static final String CONFLICT_CODE = "409";
    public static final String INTERNAL_ERROR_CODE = "500";

    //GENERIC MESSAGES
    public static final String VALIDATION_MESSAGE= "Error de validación en los datos de entrada";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor";


    //DOCUMENTATION USER
    public static final String USER_CREATE_MESSAGE = "Usuario registrado exitosamente";
    public static final String USER_CONFLICT_MESSAGE = "El usuario ya se encuentra registrado";

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

    public static final String USER_PATH_OPERATION_ID = "registerUser";
    public static final String USER_PATH_OPERATION_SUMMARY = "Registrar un nuevo usuario";
    public static final String USER_PATH_OPERATION_DESCRIPTION = "Permite registrar un nuevo solicitante proporcionando sus datos personales";
    public static final String USER_PATH_REQUEST_BODY_DESCRIPTION = "Datos del nuevo usuario";


    //DOCUMENTATION LOAN
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

    public static final String LOAN_ROUTER_OPERATION_POST = "createLoan";
    public static final String LOAN_ROUTER_OPERATION_GET = "getLoanList";
    public static final String LOAN_ROUTER_SUMMARY = "Registrar una nueva solicitud de préstamo";
    public static final String LOAN_ROUTER_DESCRIPTION = "Permite registrar una solicitud de préstamo proporcionando documento del cliente, monto, plazo y tipo de préstamo";
    public static final String LOAN_ROUTER_REQUEST_DESCRIPTION = "Datos de la nueva solicitud de préstamo";


    //DOCUMENTATION AUTH
    public static final String AUTH_SUCCESS_MESSAGE = "Login exitoso";
    public static final String AUTH_VALIDATION_MESSAGE = "Error de validación en las credenciales";
    public static final String AUTH_INVALID_CREDENTIALS_MESSAGE = "Credenciales incorrectas";

    public static final String AUTH_TEXT_VALID_LOGIN = "Login Correcto";
    public static final String AUTH_EXAMPLE_VALID_LOGIN = "{ \"code\": \"200.01\", \"message\": \"Login exitoso\", \"data\": { \"token\": \"eyJhbGciOiJIUzUxMiJ9...\" } }";
    public static final String AUTH_EXAMPLE_VALIDATION_ERROR = "{ \"code\": \"400.01\", \"message\": \"Error de validación\", \"errors\": [{ \"field\": \"email\", \"description\": \"Correo inválido\" }] }";
    public static final String AUTH_TEXT_ERROR_AUTH = "Error de autenticación";
    public static final String AUTH_EXAMPLE_ERROR_AUTH = "{ \"code\": \"401.01\", \"message\": \"Credenciales inválidas\" }";

    public static final String AUTH_PATH_OPERATION_ID = "loginUser";
    public static final String AUTH_PATH_OPERATION_SUMMARY = "Autenticar usuario";
    public static final String AUTH_PATH_OPERATION_DESCRIPTION = "Permite iniciar sesión proporcionando las credenciales de acceso";
    public static final String AUTH_PATH_REQUEST_BODY_DESCRIPTION = "Credenciales de acceso del usuario";

}
