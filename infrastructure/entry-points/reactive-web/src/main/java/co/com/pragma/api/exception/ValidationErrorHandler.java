package co.com.pragma.api.exception;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.model.user.constants.AppMessages;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationErrorHandler {
    private ValidationErrorHandler() {
        throw new UnsupportedOperationException(String.valueOf(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED));
    }

    public static Mono<? extends ServerResponse> buildValidationErrorResponse(Set<? extends ConstraintViolation<?>> violations) {
        List<Map<String, String>> errors = violations.stream()
                .map(violation -> Map.of(
                        String.valueOf(AppMessages.VALIDATOR_HANDLE_FIELD), violation.getPropertyPath().toString(),
                        String.valueOf(AppMessages.VALIDATOR_HANDLE_MESSAGE), violation.getMessage()))
                .collect(Collectors.toList());

        ApiResponse<Object> response = ApiResponse.builder()
                .code(UserUtils.VALIDATION_CODE)
                .message(UserUtils.VALIDATION_MESSAGE)
                .errors(errors)
                .build();

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
