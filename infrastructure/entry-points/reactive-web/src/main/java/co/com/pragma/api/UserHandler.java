package co.com.pragma.api;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.dto.UserRequestDTO;
import co.com.pragma.api.exception.UserUtils;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
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
public class UserHandler {
    private final RegisterUserUseCase registerUserUseCase;
    private final Validator validator;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class).flatMap(dto -> {
            var violations = validator.validate(dto);

            if (!violations.isEmpty()) {
                List<Map<String, String>> errors = violations.stream().map(v -> Map.of(
                        "field", v.getPropertyPath().toString(),
                        "description", v.getMessage()
                    )).collect(Collectors.toList());

                ApiResponse<Object> response = ApiResponse.builder().code(UserUtils.VALIDATION_CODE).message(UserUtils.VALIDATION_MESSAGE).errors(errors).build();
                return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            }

            User user = User.builder()
                .documentNumber(dto.getDocumentNumber())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .baseSalary(dto.getBaseSalary())
                .build();

            return registerUserUseCase.registerUser(user).flatMap(savedUser -> {
                ApiResponse<User> response = ApiResponse.<User>builder().code(UserUtils.CREATE_CODE).message(UserUtils.CREATE_MESSAGE).data((User) savedUser).build();
                return ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            });
        })
        .onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder().code(UserUtils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }
}
