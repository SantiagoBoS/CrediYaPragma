package co.com.pragma.model.user.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void shouldCreateBusinessExceptionWithMessage() {
        String errorMessage = "Error de negocio";
        BusinessException ex = new BusinessException(errorMessage);

        assertThat(ex)
                .isInstanceOf(RuntimeException.class)
                .hasMessage(errorMessage);
    }
}
