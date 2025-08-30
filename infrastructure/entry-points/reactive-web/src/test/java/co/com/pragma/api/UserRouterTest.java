package co.com.pragma.api;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@ContextConfiguration(classes = {UserRouter.class, UserHandler.class})
@WebFluxTest
class UserRouterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    private User validUser;
    private User invalidUser;

    @BeforeEach
    void setup() {
        validUser = User.builder()
                .id("1")
                .documentNumber("123456789")
                .name("Santiago")
                .lastName("Test")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("123456789")
                .email("santiago@example.com")
                .baseSalary(new BigDecimal("1000000"))
                .build();

        invalidUser = new User("", "", "", "", null, "", "", "bad-email", null);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        Mockito.when(registerUserUseCase.registerUser(Mockito.any(User.class)))
            .thenReturn(Mono.just(validUser));

        webTestClient.post()
            .uri("/api/v1/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(validUser)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.data.email").isEqualTo("santiago@example.com")
            .jsonPath("$.status").isEqualTo("OK");
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        Mockito.when(registerUserUseCase.registerUser(Mockito.any(User.class)))
            .thenReturn(Mono.error(new BusinessException("Rellena todos los campos obligatorios")));

        webTestClient.post()
            .uri("/api/v1/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidUser)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Rellena todos los campos obligatorios")
            .jsonPath("$.status").isEqualTo("BAD_REQUEST");
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() {
        Mockito.when(registerUserUseCase.registerUser(Mockito.any(User.class)))
            .thenReturn(Mono.error(new BusinessException("El correo electrónico ya se encuentra registrado")));

        webTestClient.post()
            .uri("/api/v1/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(validUser)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("El correo electrónico ya se encuentra registrado")
            .jsonPath("$.status").isEqualTo("BAD_REQUEST");
    }
}
