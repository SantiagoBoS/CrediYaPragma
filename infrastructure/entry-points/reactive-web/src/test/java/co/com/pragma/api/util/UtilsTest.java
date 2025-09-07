package co.com.pragma.api.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {
    @Test
    void shouldContainExpectedGeneralConstants() {
        assertThat(Utils.SUCCESS_CODE).isEqualTo("200.00");
        assertThat(Utils.SUCCESS_MESSAGE).isEqualTo("OK");

        assertThat(Utils.VALIDATION_CODE).isEqualTo("400.01");
        assertThat(Utils.VALIDATION_CODE_GENERAL).isEqualTo("400.02");
        assertThat(Utils.INTERNAL_ERROR_CODE).isEqualTo("500.01");
        assertThat(Utils.INTERNAL_ERROR_MESSAGE).isEqualTo("Error interno del servidor");
    }

    @Test
    void shouldContainExpectedUserConstants() {
        assertThat(Utils.USER_CREATE_MESSAGE).isEqualTo("Usuario registrado exitosamente");
        assertThat(Utils.USER_CONFLICT_MESSAGE).isEqualTo("El usuario ya se encuentra registrado.");
        assertThat(Utils.USER_PATH_API_USERS).isEqualTo("/api/v1/usuarios");
        assertThat(Utils.USER_PATH_OPERATION_ID).isEqualTo("registerUser");
    }

    @Test
    void shouldContainExpectedLoanConstants() {
        assertThat(Utils.LOAN_CREATE_MESSAGE).isEqualTo("Solicitud registrada correctamente");
        assertThat(Utils.LOAN_CONFLICT_MESSAGE).isEqualTo("Ya existe una solicitud en proceso para este cliente");
        assertThat(Utils.LOAN_ROUTER_BASE_PATH).isEqualTo("/api/v1/solicitud");
        assertThat(Utils.LOAN_ROUTER_OPERATION_ID).isEqualTo("createLoan");
    }
}
