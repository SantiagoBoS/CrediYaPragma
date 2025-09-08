package co.com.pragma.api.user;

import co.com.pragma.api.util.Utils;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

@ContextConfiguration(classes = {UserRouter.class, UserHandler.class})
@WebFluxTest
class UserRouterTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserUseCase userUseCase;

    @MockBean
    private UserRepository userRepository;

    private User validUser;
    private User invalidUser;
    private String CODE = "$.code";
    private String errorValidationMessage = "Error de validación en los datos de entrada.";
    private String emailExistsMessage = "El correo electrónico ya se encuentra registrado";

    @BeforeEach
    void setup() {
        validUser = User.builder()
                .documentNumber("123456789")
                .name("Santiago")
                .lastName("Test")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phone("123456789")
                .email("santiago@example.com")
                .baseSalary(new BigDecimal("1000000"))
                .build();

        invalidUser = new User("", "", "", null, "", "", "bad-email", null);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        Mockito.when(userUseCase.registerUser(Mockito.any(User.class))).thenReturn(Mono.just(validUser));

        webTestClient.post()
            .uri(Utils.USER_PATH_API_USERS)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(validUser)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.data.email").isEqualTo("santiago@example.com")
            .jsonPath(CODE).isEqualTo("201.01");
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        Mockito.when(userUseCase.registerUser(Mockito.any(User.class))).thenReturn(Mono.error(new BusinessException(errorValidationMessage)));

        webTestClient.post()
            .uri(Utils.USER_PATH_API_USERS)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(invalidUser)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo(errorValidationMessage)
            .jsonPath(CODE).isEqualTo("400.01");
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() {
        Mockito.when(userUseCase.registerUser(Mockito.any(User.class)))
                .thenReturn(Mono.error(new BusinessException(emailExistsMessage)));

        webTestClient.post()
            .uri(Utils.USER_PATH_API_USERS)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(validUser)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo(emailExistsMessage)
            .jsonPath(CODE).isEqualTo("400.02");
    }
}
