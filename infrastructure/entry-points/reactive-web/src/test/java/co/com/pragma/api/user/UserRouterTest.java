package co.com.pragma.api.user;

import co.com.pragma.api.user.dto.UserDTO;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserRouterTest {

    private WebTestClient webTestClient;
    private UserUseCase userUseCase;
    private UserRepository userRepository;
    private UserDTO dto;

    @BeforeEach
    void setUp() {
        userUseCase = Mockito.mock(UserUseCase.class);
        userRepository = Mockito.mock(UserRepository.class);

        dto = UserDTO.builder()
                .documentNumber("123")
                .name("Test")
                .lastName("Apellido")
                .email("test@test.com")
                .password("secret")
                .role("CLIENT")
                .baseSalary(BigDecimal.valueOf(2000))
                .build();

        UserHandler handler = new UserHandler(
                userUseCase,
                Validation.buildDefaultValidatorFactory().getValidator(),
                userRepository
        );

        RouterFunction<ServerResponse> routes = new UserRouter().userRoutes(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void testRoutesAreRegistered() {
        when(userUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.just(User.builder().documentNumber("123").build()));

        webTestClient.post()
                .uri(ApiPaths.USER_BASE)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated();

        when(userRepository.findByDocumentNumber("123"))
                .thenReturn(Mono.just(User.builder().documentNumber("123").build()));

        webTestClient.get()
                .uri(ApiPaths.USER_BY_DOCUMENT.replace("{documentNumber}", "123"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testRegisterUserReturnsUserInResponse() {
        when(userUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.just(User.builder().documentNumber("123").name("Test").build()));

        webTestClient.post()
                .uri(ApiPaths.USER_BASE)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.documentNumber").isEqualTo("123")
                .jsonPath("$.data.name").isEqualTo("Test");
    }
}