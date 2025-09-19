package co.com.pragma.api.auth;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.auth.dto.AuthRequestDTO;
import co.com.pragma.api.exception.ValidationErrorHandler;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.constants.ErrorCode;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.usecase.auth.AuthUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

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
                    ApiResponse<Object> response = ApiResponse.builder().code(ErrorCode.AUTH_CREATE_CODE.getBusinessCode())
                            .message(AppMessages.USER_FOUND.getMessage()).data(Map.of("token", token)).build();
                    return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                });
        }).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder().code(ErrorCode.AUTH_GENERAL_VALIDATION_ERROR.getBusinessCode()).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }
}
