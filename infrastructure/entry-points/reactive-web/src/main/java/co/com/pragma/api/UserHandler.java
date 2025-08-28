package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final RegisterUserUseCase registerUserUseCase;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(User.class)
            .flatMap(registerUserUseCase::registerUser)
            .flatMap(user -> ServerResponse.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(ApiResponse.<User>builder()
                        .status("OK")
                        .message("Usuario registrado correctamente")
                        .data(user)
                        .build()))
            .onErrorResume(e -> {
                String status = "ERROR";
                HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

                if (e instanceof BusinessException) {
                    String msg = e.getMessage();
                    if (msg.contains("correo electrónico ya está registrado")) {
                        status = "CONFLICT";
                        httpStatus = HttpStatus.CONFLICT;
                    } else {
                        status = "BAD_REQUEST";
                        httpStatus = HttpStatus.BAD_REQUEST;
                    }
                }

                ApiResponse<?> errorResponse = ApiResponse.builder()
                    .status(status)
                    .message(e.getMessage())
                    .data(null)
                    .build();

                return ServerResponse.status(httpStatus)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(errorResponse);
            });
    }
}
