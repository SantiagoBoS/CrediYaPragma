package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.AuthRequestDTO;
import co.com.pragma.api.exception.AuthUtils;
import co.com.pragma.api.exception.ValidationErrorHandler;
import co.com.pragma.model.auth.exception.BusinessException;
import co.com.pragma.usecase.auth.AuthUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthHandler {
    private final AuthUseCase authUseCase;
    private final Validator validator;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequestDTO.class).flatMap(dto -> {
            var violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                return ValidationErrorHandler.buildValidationErrorResponse(violations);
            }

            return authUseCase.login(dto.getEmail(), dto.getPassword()).flatMap(token -> {
                    ApiResponse<Object> response = ApiResponse.builder().code(AuthUtils.CREATE_CODE)
                            .message(AuthUtils.CREATE_MESSAGE).data(Map.of("token", token)).build();
                    return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
        }).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder().code(AuthUtils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }
}
