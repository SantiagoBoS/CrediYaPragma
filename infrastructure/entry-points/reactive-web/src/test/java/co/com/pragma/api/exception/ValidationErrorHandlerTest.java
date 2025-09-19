package co.com.pragma.api.exception;

import co.com.pragma.model.constants.AppMessages;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationErrorHandlerTest {

    @Test
    void shouldThrowExceptionWhenInstantiated() {
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                ValidationErrorHandler::new
        );
        assertTrue(exception.getMessage().contains(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage()));
    }
}
