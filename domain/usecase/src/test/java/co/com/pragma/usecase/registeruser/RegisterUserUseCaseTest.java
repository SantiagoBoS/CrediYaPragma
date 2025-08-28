package co.com.pragma.usecase.registeruser;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RegisterUserUseCaseTest {

    private UserRepository userRepository;
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        registerUserUseCase = new RegisterUserUseCase(userRepository);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        User user = new User(
            null,
            "Juan",
            "Pérez",
            LocalDate.parse("1990-05-15"),
            "Calle 123",
            "3102567890",
            "juan.perez@example.com",
            new BigDecimal("2000000")
        );

        // mockeamos que no existe el usuario y que al guardar devuelve el mismo
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        Mockito.when(userRepository.save(user)).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(registerUserUseCase.registerUser(user))
                .expectNext(user)
                .verifyComplete();
    }

}
