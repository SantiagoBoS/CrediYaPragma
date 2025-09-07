package co.com.pragma.api.loan.util;

import co.com.pragma.api.util.Utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoanUtilsTest {
    @Test
    void constantsShouldHaveExpectedValues() {
        assertEquals("201.01", Utils.CREATE_CODE);
        assertEquals("Solicitud registrada correctamente", LoanUtils.CREATE_MESSAGE);

        assertEquals("400.01", Utils.VALIDATION_CODE);
        assertEquals("400.02", Utils.VALIDATION_CODE_GENERAL);
        assertEquals("Error de validación en los datos de entrada.", Utils.VALIDATION_MESSAGE);

        assertEquals("409.01", Utils.CONFLICT_CODE);
        assertEquals("Ya existe una solicitud en proceso para este cliente", LoanUtils.CONFLICT_MESSAGE);

        assertEquals("500.01", Utils.INTERNAL_ERROR_CODE);
        assertEquals("Error interno del servidor", Utils.INTERNAL_ERROR_MESSAGE);

        assertEquals("/api/v1/solicitud", LoanUtils.ROUTER_BASE_PATH);
        assertEquals("createLoan", LoanUtils.ROUTER_OPERATION_ID);
        assertEquals("Registrar una nueva solicitud de préstamo", LoanUtils.ROUTER_SUMMARY);
    }
}
