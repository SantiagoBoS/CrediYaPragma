package co.com.pragma.model.exceptions;

import co.com.pragma.model.constants.AppMessages;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void shouldCreateExceptionWithStringMessage() {
        //Validar que se cree la excepcion con un mensaje personalizado
        String message = "Custom error occurred";
        BusinessException exception = new BusinessException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithAppMessagesEnum() {
        //Validar que se cree la excepcion con un mensaje del enum AppMessages
        AppMessages mockMessage = AppMessages.LOAN_TYPE_INVALID;
        BusinessException exception = new BusinessException(mockMessage);

        assertEquals(mockMessage.getMessage(), exception.getMessage());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        //Validar que BusinessException es una subclase de RuntimeException
        BusinessException exception = new BusinessException("Test error");
        assertTrue(exception instanceof RuntimeException);
    }
}
