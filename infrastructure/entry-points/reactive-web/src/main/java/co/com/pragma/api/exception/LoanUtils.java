package co.com.pragma.api.exception;

public class LoanUtils {
    public static final String CREATE_CODE = "201.01";
    public static final String CREATE_MESSAGE = "Solicitud registrada correctamente";

    public static final String VALIDATION_CODE = "400.01";
    public static final String VALIDATION_CODE_GENERAL = "400.02";
    public static final String VALIDATION_MESSAGE = "Error de validación en los datos de entrada.";

    public static final String CONFLICT_CODE = "409.01";
    public static final String CONFLICT_MESSAGE = "Ya existe una solicitud en proceso para este cliente";

    public static final String INTERNAL_ERROR_CODE = "500.01";
    public static final String INTERNAL_ERROR_MESSAGE = "Error interno del servidor";


    public static final String TEXT_VALID_LOAN = "Solicitud válida";
    public static final String EXAMPLE_VALID_LOAN = "{ \"clientDocument\": \"123456789\", \"amount\": 5000000, \"term\": 24, \"loanType\": \"PERSONAL\" }";
    public static final String TEXT_VALIDATION_CORRECT = "Solicitud creada";
    public static final String EXAMPLE_VALIDATION_CORRECT = "{ \"code\": \"201.01\", \"message\": \"Solicitud registrada exitosamente\", \"data\": { \"clientDocument\": \"123456789\", \"amount\": 5000000, \"term\": 24, \"loanType\": \"PERSONAL\", \"status\": \"PENDING_REVIEW\" } }";
    public static final String TEXT_ERROR_VALIDATION =  "Error de validación";
    public static final String EXAMPLE_ERROR_VALIDATION = "{ \"code\": \"400.01\", \"message\": \"Error de validación en los datos de entrada.\", \"errors\": [ \"El campo 'amount' debe ser mayor a 0\", \"El campo 'term' es obligatorio\" ] }";
    public static final String EXAMPLE_VALIDATION_DUPLICATE = "{ \"code\": \"409.01\", \"message\": \"El cliente ya tiene una solicitud en proceso\" }";
    public static final String EXAMPLE_INTERNAL_ERROR = "{ \"code\": \"500.01\", \"message\": \"Ocurrió un error inesperado. Intente nuevamente más tarde.\" }";

    public static final String ROUTER_BASE_PATH = "/api/v1/solicitud";
    public static final String ROUTER_OPERATION_ID = "createLoanRequest";
    public static final String ROUTER_SUMMARY = "Registrar una nueva solicitud de préstamo";
    public static final String ROUTER_DESCRIPTION = "Permite registrar una solicitud de préstamo proporcionando documento del cliente, monto, plazo y tipo de préstamo";
    public static final String ROUTER_REQUEST_DESCRIPTION = "Datos de la nueva solicitud de préstamo";
}
