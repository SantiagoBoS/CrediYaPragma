package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.UserRequestDTO;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.xmlunit.validation.Validator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final RegisterUserUseCase registerUserUseCase;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class)
                .map(dto -> User.builder()
                        .documentNumber(dto.getDocumentNumber())
                        .name(dto.getName())
                        .lastName(dto.getLastName())
                        .birthDate(dto.getBirthDate())
                        .address(dto.getAddress())
                        .phone(dto.getPhone())
                        .email(dto.getEmail())
                        .baseSalary(dto.getBaseSalary())
                        .build())
                .flatMap(registerUserUseCase::registerUser)
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.<User>builder()
                                .status("OK")
                                .message("Usuario registrado correctamente")
                                .data(user)
                                .build()));
    }
}
