package co.com.pragma.api.user;

import co.com.pragma.api.user.dto.UserRequestDTO;
import co.com.pragma.api.user.util.UserUtils;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserHandlerTest {

    private RegisterUserUseCase registerUserUseCase;
    private WebTestClient webTestClient;
    private UserRequestDTO dto;
    private String CODE = "$.code";
    private String MESSAGE = "$.message";

    @BeforeEach
    void setUp() {
        dto = UserRequestDTO.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 123")
                .baseSalary(BigDecimal.valueOf(5000.0))
                .build();

        registerUserUseCase = Mockito.mock(RegisterUserUseCase.class);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserHandler handler = new UserHandler(registerUserUseCase, validator);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route().POST(UserUtils.PATH_API_USERS, handler::registerUser).build();
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        User savedUser = User.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 123")
                .baseSalary(BigDecimal.valueOf(5000.0))
                .build();

        when(registerUserUseCase.registerUser(any(User.class))).thenReturn(Mono.just(savedUser));
        webTestClient.post()
                .uri(UserUtils.PATH_API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath(CODE).isEqualTo(UserUtils.CREATE_CODE)
                .jsonPath(MESSAGE).isEqualTo(UserUtils.CREATE_MESSAGE)
                .jsonPath("$.data.name").isEqualTo("Santiago");

        verify(registerUserUseCase, times(1)).registerUser(any(User.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        UserRequestDTO invalidDto = UserRequestDTO.builder().build();
        when(registerUserUseCase.registerUser(any(User.class))).thenReturn(Mono.error(new BusinessException(UserUtils.VALIDATION_MESSAGE)));
        webTestClient.post()
                .uri(UserUtils.PATH_API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath(CODE).isEqualTo(UserUtils.VALIDATION_CODE)
                .jsonPath(MESSAGE).isEqualTo(UserUtils.VALIDATION_MESSAGE)
                .jsonPath("$.errors").isArray();

        verify(registerUserUseCase, times(1)).registerUser(any(User.class));
    }

    @Test
    void shouldHandleBusinessException() {
        when(registerUserUseCase.registerUser(any(User.class))).thenReturn(Mono.error(new BusinessException(UserUtils.VALIDATION_ERROR_USER_EXISTS)));
        webTestClient.post()
                .uri(UserUtils.PATH_API_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath(CODE).isEqualTo(UserUtils.VALIDATION_CODE_GENERAL)
                .jsonPath(MESSAGE).isEqualTo(UserUtils.VALIDATION_ERROR_USER_EXISTS);
    }
}
