package co.com.pragma.api.exception;

import co.com.pragma.api.util.UserUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserUtilsTest {

    @Test
    void shouldHaveCorrectCreateConstants() {
        assertThat(UserUtils.CREATE_CODE).isEqualTo("201.01");
        assertThat(UserUtils.CREATE_MESSAGE).isEqualTo("Usuario registrado exitosamente");
    }

    @Test
    void shouldHaveCorrectValidationConstants() {
        assertThat(UserUtils.VALIDATION_CODE).isEqualTo("400.01");
        assertThat(UserUtils.VALIDATION_CODE_GENERAL).isEqualTo("400.02");
        assertThat(UserUtils.VALIDATION_MESSAGE).isEqualTo("Error de validación en los datos de entrada.");
    }

    @Test
    void shouldHaveCorrectConflictConstants() {
        assertThat(UserUtils.CONFLICT_CODE).isEqualTo("409.01");
        assertThat(UserUtils.CONFLICT_MESSAGE).isEqualTo("El usuario ya se encuentra registrado.");
    }

    @Test
    void shouldHaveCorrectInternalErrorConstants() {
        assertThat(UserUtils.INTERNAL_ERROR_CODE).isEqualTo("500.01");
        assertThat(UserUtils.INTERNAL_ERROR_MESSAGE).isEqualTo("Error interno del servidor");
    }
}
