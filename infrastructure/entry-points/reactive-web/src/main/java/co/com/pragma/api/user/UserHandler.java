package co.com.pragma.api.user;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.user.dto.UserRequestDTO;
import co.com.pragma.api.user.mapper.UserMapper;
import co.com.pragma.api.user.util.UserUtils;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import jakarta.validation.Validator;
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
    private final Validator validator;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRequestDTO.class).flatMap(dto ->
                ValidationUtils.validate(dto, validator).switchIfEmpty(
                        registerUserUseCase.registerUser(UserMapper.toEntity(dto)).flatMap(savedUser -> {
                            ApiResponse<User> response = ApiResponse.<User>builder()
                                    .code(UserUtils.CREATE_CODE).message(UserUtils.CREATE_MESSAGE)
                                    .data(savedUser).build();
                            return ServerResponse.status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                        })
                )
        ).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder()
                    .code(UserUtils.VALIDATION_CODE_GENERAL).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }
}
