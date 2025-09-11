package co.com.pragma.api.util;

import co.com.pragma.api.exception.ValidationErrorHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

public class ValidationUtils {
    public static <T> Mono<ServerResponse> validate(T dto, Validator validator) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        return violations.isEmpty() ? Mono.empty()
                : ValidationErrorHandler.buildValidationErrorResponse(violations);
    }
}
