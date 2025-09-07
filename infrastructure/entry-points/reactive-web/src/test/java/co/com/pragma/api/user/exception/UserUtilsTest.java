package co.com.pragma.api.user.exception;

import co.com.pragma.api.util.Utils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserUtilsTest {

    @Test
    void shouldHaveCorrectCreateConstants() {
        assertThat(Utils.CREATE_CODE).isEqualTo("201.01");
        assertThat(Utils.USER_CREATE_MESSAGE).isEqualTo("Usuario registrado exitosamente");
    }

    @Test
    void shouldHaveCorrectValidationConstants() {
        assertThat(Utils.VALIDATION_CODE).isEqualTo("400.01");
        assertThat(Utils.VALIDATION_CODE_GENERAL).isEqualTo("400.02");
        assertThat(Utils.VALIDATION_MESSAGE).isEqualTo("Error de validación en los datos de entrada.");
    }

    @Test
    void shouldHaveCorrectConflictConstants() {
        assertThat(Utils.CONFLICT_CODE).isEqualTo("409.01");
        assertThat(Utils.USER_CONFLICT_MESSAGE).isEqualTo("El usuario ya se encuentra registrado.");
    }

    @Test
    void shouldHaveCorrectInternalErrorConstants() {
        assertThat(Utils.INTERNAL_ERROR_CODE).isEqualTo("500.01");
        assertThat(Utils.INTERNAL_ERROR_MESSAGE).isEqualTo("Error interno del servidor");
    }
}
