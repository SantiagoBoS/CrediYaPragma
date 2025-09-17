package co.com.pragma.api.auth;

import co.com.pragma.api.auth.dto.AuthRequestDTO;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.constants.ErrorCode;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.usecase.auth.AuthUseCase;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthHandlerTest {

    private AuthUseCase authUseCase;
    private Validator validator;
    private WebTestClient webTestClient;

    private AuthRequestDTO authRequest;

    @BeforeEach
    void setUp() {
        authUseCase = mock(AuthUseCase.class);
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        AuthHandler handler = new AuthHandler(authUseCase, validator);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .POST("/auth/login", handler::login)
                .build();

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        authRequest = AuthRequestDTO.builder()
                .email("test@test.com")
                .password("123456")
                .build();
    }

    @Test
    void shouldLoginSuccessfully() {
        //Verifica que el login se haga correctamente
        String fakeToken = "fake-jwt-token";
        when(authUseCase.login(anyString(), anyString())).thenReturn(Mono.just(fakeToken));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCode.AUTH_CREATE_CODE.getBusinessCode())
                .jsonPath("$.message").isEqualTo(AppMessages.USER_FOUND.getMessage())
                .jsonPath("$.data.token").isEqualTo(fakeToken);

        verify(authUseCase, times(1)).login("test@test.com", "123456");
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        //Verifica que la solicitud ha sido incorrecta cuando falla la validacion
        AuthRequestDTO invalidRequest = AuthRequestDTO.builder().build();

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCode.VALIDATION_ERROR.getBusinessCode())
                .jsonPath("$.errors").isArray();
    }

    @Test
    void shouldReturnUnauthorizedWhenBusinessExceptionThrown() {
        //Valida que devuelve un no autorizado cuando lanza la excepcion
        when(authUseCase.login(anyString(), anyString())).thenReturn(Mono.error(new BusinessException("Invalid credentials")));

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCode.AUTH_GENERAL_VALIDATION_ERROR.getBusinessCode())
                .jsonPath("$.message").isEqualTo("Invalid credentials");

        verify(authUseCase, times(1)).login("test@test.com", "123456");
    }
}
