package co.com.pragma.api.user;

import co.com.pragma.api.dto.ApiResponse;
import co.com.pragma.api.user.dto.UserDTO;
import co.com.pragma.api.user.mapper.UserMapper;
import co.com.pragma.api.util.ValidationUtils;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.constants.ErrorCode;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserUseCase registerUserUseCase;
    private final Validator validator;
    private final UserRepository userRepository;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserDTO.class).flatMap(dto ->
                ValidationUtils.validate(dto, validator).switchIfEmpty(
                        registerUserUseCase.registerUser(UserMapper.toEntity(dto)).flatMap(savedUser -> {
                            ApiResponse<User> response = ApiResponse.<User>builder()
                                    .code(ErrorCode.USER_CREATED.getBusinessCode()).message(AppMessages.USER_CREATED.getMessage())
                                    .data(savedUser).build();
                            return ServerResponse.status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON).bodyValue(response);
                        })
                )
        ).onErrorResume(BusinessException.class, ex -> {
            ApiResponse<Object> response = ApiResponse.builder().code(ErrorCode.USER_GENERAL_VALIDATION_ERROR.getBusinessCode()).message(ex.getMessage()).build();
            return ServerResponse.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).bodyValue(response);
        });
    }

    public Mono<ServerResponse> getUserByDocument(ServerRequest request) {
        String documentNumber = request.pathVariable("documentNumber");
        return userRepository.findByDocumentNumber(documentNumber)
            .timeout(Duration.ofSeconds(3))
            .flatMap(user -> {ApiResponse<User> response = ApiResponse.<User>builder()
                    .code(ErrorCode.USER_SUCCESS.getBusinessCode()).message(AppMessages.USER_FOUND.getMessage())
                    .data(user).build();
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response);
            }).switchIfEmpty(ServerResponse.notFound().build());
    }
}
