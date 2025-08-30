package co.com.pragma.api.config;

import co.com.pragma.api.UserHandler;
import co.com.pragma.api.UserRouter;
import co.com.pragma.usecase.registeruser.RegisterUserUseCase;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {UserRouter.class, UserHandler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = new User("", "123456789", "Santiago","Borrero",
                LocalDate.parse("1990-12-12"), "Cra 8 # 45-67", "3001234567",
                "santi@example.com", BigDecimal.valueOf(5000000));

        given(registerUserUseCase.registerUser(any(User.class))).willReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("Santiago")
                .jsonPath("$.data.email").isEqualTo("santi@example.com");
    }

    @Test
    void shouldReturnBadRequestWhenBusinessExceptionThrown() {
        User invalidUser = new User("", "", "", "", null,"", "", "bad-email", null);

        given(registerUserUseCase.registerUser(any(User.class)))
                .willReturn(Mono.error(new BusinessException("Rellena todos los campos obligatorios")));

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUser)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Rellena todos los campos obligatorios");
    }
}