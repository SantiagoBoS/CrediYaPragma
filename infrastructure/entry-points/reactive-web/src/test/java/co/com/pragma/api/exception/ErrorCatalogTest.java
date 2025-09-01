package co.com.pragma.api.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorCatalogTest {

    @Test
    void shouldHaveCorrectCreateConstants() {
        assertThat(ErrorCatalog.CREATE_CODE).isEqualTo("201.01");
        assertThat(ErrorCatalog.CREATE_MESSAGE).isEqualTo("Usuario registrado exitosamente");
    }

    @Test
    void shouldHaveCorrectValidationConstants() {
        assertThat(ErrorCatalog.VALIDATION_CODE).isEqualTo("400.01");
        assertThat(ErrorCatalog.VALIDATION_CODE_GENERAL).isEqualTo("400.02");
        assertThat(ErrorCatalog.VALIDATION_MESSAGE).isEqualTo("Error de validación en los datos de entrada.");
    }

    @Test
    void shouldHaveCorrectConflictConstants() {
        assertThat(ErrorCatalog.CONFLICT_CODE).isEqualTo("409.01");
        assertThat(ErrorCatalog.CONFLICT_MESSAGE).isEqualTo("El usuario ya se encuentra registrado.");
    }

    @Test
    void shouldHaveCorrectInternalErrorConstants() {
        assertThat(ErrorCatalog.INTERNAL_ERROR_CODE).isEqualTo("500.01");
        assertThat(ErrorCatalog.INTERNAL_ERROR_MESSAGE).isEqualTo("Error interno del servidor.");
    }
}
