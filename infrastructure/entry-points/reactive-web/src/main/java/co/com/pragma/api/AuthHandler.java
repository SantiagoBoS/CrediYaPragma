package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.AuthRequestDTO;
import co.com.pragma.api.exception.AuthUtils;
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
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandler {
    private final AuthUseCase authUseCase;
    private final Validator validator;

    public Mono<ServerResponse> login(ServerRequest request) {
        log.info("AuthHandler - Intento de login recibido");
        return request.bodyToMono(AuthRequestDTO.class).flatMap(dto -> {
            log.debug("AuthHandler - Validando DTO para email={}", dto.getEmail());
            var violations = validator.validate(dto);

            if (!violations.isEmpty()) {
                log.warn("AuthHandler - Violaciones de validacion encontradas: {}", violations.size());
                List<Map<String, String>> errors = violations.stream().map(v -> Map.of(
                        "field", v.getPropertyPath().toString(),
                        "description", v.getMessage()
                )).collect(Collectors.toList());

                ApiResponse<Object> response = ApiResponse.builder().code(AuthUtils.VALIDATION_CODE).message(AuthUtils.VALIDATION_MESSAGE).errors(errors).build();
                return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            }

            log.info("Credenciales recibidas correctamente para email={}", dto.getEmail());
            return authUseCase.login(dto.getEmail(), dto.getPassword())
                    .flatMap(token -> {
                        log.info("AuthHandler - Login exitoso para email={}", dto.getEmail());
                        ApiResponse<Object> response = ApiResponse.builder().code(AuthUtils.CREATE_CODE).message(AuthUtils.CREATE_MESSAGE).data(Map.of("token", token)).build();
                        return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                    });
        }).onErrorResume(BusinessException.class, ex -> {
            log.error("AuthHandler - Error de negocio en login: {}", ex.getMessage());
            ApiResponse<Object> response = ApiResponse.builder().code(AuthUtils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        }).doOnTerminate(() -> log.debug("AuthHandler - Finalizo el flujo de login"));
    }
}
