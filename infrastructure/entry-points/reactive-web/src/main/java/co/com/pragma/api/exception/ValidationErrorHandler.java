package co.com.pragma.api.exception;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.model.loan.constants.AppMessages;
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

    public static Mono<ServerResponse> buildValidationErrorResponse(Set<? extends ConstraintViolation<?>> violations) {
        List<Map<String, String>> errors = violations.stream()
                .map(violation -> Map.of(
                        String.valueOf(AppMessages.VALIDATOR_HANDLE_FIELD), violation.getPropertyPath().toString(),
                        String.valueOf(AppMessages.VALIDATOR_HANDLE_MESSAGE), violation.getMessage()))
                .collect(Collectors.toList());

        ApiResponse<Object> response = ApiResponse.builder()
                .code(LoanRequestUtils.VALIDATION_CODE)
                .message(LoanRequestUtils.VALIDATION_MESSAGE)
                .errors(errors)
                .build();

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}
