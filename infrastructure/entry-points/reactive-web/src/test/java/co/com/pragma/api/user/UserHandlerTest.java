package co.com.pragma.api.user;

import co.com.pragma.api.user.dto.UserDTO;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.constants.ErrorCode;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
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

    private UserUseCase userUseCase;
    private WebTestClient webTestClient;
    private UserRepository userRepository;
    private UserDTO dto;
    private User savedUser;
    private String CODE = "$.code";
    private String MESSAGE = "$.message";
    private static final String userAlready = "Usuario ya existe";


    @BeforeEach
    void setUp() {
        dto = UserDTO.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 123")
                .baseSalary(BigDecimal.valueOf(5000.0))
                .build();

        savedUser = User.builder()
                .documentNumber("12345")
                .name("Santiago")
                .lastName("Test")
                .email("test@test.com")
                .phone("3123456789")
                .address("Calle 123")
                .baseSalary(BigDecimal.valueOf(5000.0))
                .build();

        userUseCase = Mockito.mock(UserUseCase.class);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        userRepository = Mockito.mock(UserRepository.class);
        UserHandler handler = new UserHandler(userUseCase, validator, userRepository);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route().POST(ApiPaths.USER_BASE, handler::registerUser).build();
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        dto.setPassword("secret");
        dto.setRole("CLIENT");

        when(userUseCase.registerUser(any(User.class))).thenReturn(Mono.just(savedUser));

        webTestClient.post()
                .uri(ApiPaths.USER_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath(CODE).isEqualTo(ErrorCode.USER_CREATED.getBusinessCode())
                .jsonPath(MESSAGE).isEqualTo(AppMessages.USER_CREATED.getMessage())
                .jsonPath("$.data.name").isEqualTo("Santiago");

        verify(userUseCase, times(1)).registerUser(any(User.class));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() {
        UserDTO invalidDto = UserDTO.builder().build();
        when(userUseCase.registerUser(any(User.class))).thenReturn(Mono.error(new BusinessException(AppMessages.VALIDATION_MESSAGE.getMessage())));
        webTestClient.post()
                .uri(ApiPaths.USER_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath(CODE).isEqualTo(ErrorCode.VALIDATION_ERROR.getBusinessCode())
                .jsonPath(MESSAGE).isEqualTo(AppMessages.VALIDATION_MESSAGE.getMessage())
                .jsonPath("$.errors").isArray();

        verify(userUseCase, times(1)).registerUser(any(User.class));
    }

    @Test
    void shouldHandleBusinessException() {
        dto.setPassword("secret");
        dto.setRole("CLIENT");

        String userAlready = "Usuario ya existe";
        when(userUseCase.registerUser(any(User.class))).thenReturn(Mono.error(new BusinessException(userAlready)));

        webTestClient.post()
                .uri(ApiPaths.USER_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath(CODE).isEqualTo(ErrorCode.USER_GENERAL_VALIDATION_ERROR.getBusinessCode())
                .jsonPath(MESSAGE).isEqualTo(userAlready);
    }

    @Test
    void shouldReturnUserWhenFound() {
        when(userRepository.findByDocumentNumber("12345")).thenReturn(Mono.just(savedUser));
        UserHandler handler = new UserHandler(userUseCase, Validation.buildDefaultValidatorFactory().getValidator(), userRepository);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .GET("/users/{documentNumber}", handler::getUserByDocument).build();
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunction).build();

        client.get()
                .uri("/users/12345")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ErrorCode.USER_SUCCESS.getBusinessCode())
                .jsonPath("$.message").isEqualTo(AppMessages.USER_FOUND.getMessage())
                .jsonPath("$.data.documentNumber").isEqualTo("12345");

        verify(userRepository, times(1)).findByDocumentNumber("12345");
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() {
        when(userRepository.findByDocumentNumber("99999")).thenReturn(Mono.empty());
        UserHandler handler = new UserHandler(userUseCase, Validation.buildDefaultValidatorFactory().getValidator(), userRepository);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .GET("/users/{documentNumber}", handler::getUserByDocument).build();
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunction).build();

        client.get()
                .uri("/users/99999")
                .exchange()
                .expectStatus().isNotFound();
        verify(userRepository, times(1)).findByDocumentNumber("99999");
    }

    @Test
    void shouldTimeoutWhenUserRepositoryDoesNotRespond() {
        when(userRepository.findByDocumentNumber("slow-user"))
                .thenReturn(Mono.never());

        UserHandler handler = new UserHandler(userUseCase, Validation.buildDefaultValidatorFactory().getValidator(), userRepository);
        RouterFunction<ServerResponse> routerFunction = RouterFunctions.route()
                .GET("/users/{documentNumber}", handler::getUserByDocument).build();
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunction).build();

        client.get()
                .uri("/users/slow-user")
                .exchange()
                .expectStatus().is5xxServerError(); // el timeout se traduce en error
    }
}
