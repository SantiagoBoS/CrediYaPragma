package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequestDTO;
import co.com.pragma.api.exception.ErrorCatalog;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
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
    private Validator validator;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        registerUserUseCase = Mockito.mock(RegisterUserUseCase.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        UserHandler handler = new UserHandler(registerUserUseCase, validator);

        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .POST("/api/v1/usuarios", handler::registerUser).build();

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // given
        UserRequestDTO dto = UserRequestDTO.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 123")
                .baseSalary(BigDecimal.valueOf(5000.0))
                .build();

        User savedUser = User.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 123")
                .baseSalary(BigDecimal.valueOf(5000.0))
                .build();

        when(registerUserUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.just(savedUser));

        // when & then
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCatalog.CREATE_CODE)
                .jsonPath("$.message").isEqualTo(ErrorCatalog.CREATE_MESSAGE)
                .jsonPath("$.data.name").isEqualTo("Santiago");

        verify(registerUserUseCase, times(1)).registerUser(any(User.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        // given → DTO vacío (falla validación con Bean Validation)
        UserRequestDTO invalidDto = UserRequestDTO.builder().build();

        // when & then
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCatalog.VALIDATION_CODE)
                .jsonPath("$.message").isEqualTo(ErrorCatalog.VALIDATION_MESSAGE)
                .jsonPath("$.errors").isArray();
    }

    @Test
    void shouldHandleBusinessException() {
        // given
        UserRequestDTO dto = UserRequestDTO.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 456")
                .baseSalary(BigDecimal.valueOf(7000.0))
                .build();

        when(registerUserUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.error(new BusinessException("Usuario ya existe")));

        // when & then
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCatalog.VALIDATION_CODE_GENERAL)
                .jsonPath("$.message").isEqualTo("Usuario ya existe");
    }
}
