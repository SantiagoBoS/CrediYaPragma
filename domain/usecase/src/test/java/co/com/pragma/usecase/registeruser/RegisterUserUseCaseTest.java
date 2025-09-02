package co.com.pragma.usecase.registeruser;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class RegisterUserUseCaseTest {

    private UserRepository userRepository;
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        registerUserUseCase = new RegisterUserUseCase(userRepository);
    }

    private User buildUser() {
        return new User(
            "123456",
            "Santiago",
            "Test",
            LocalDate.parse("1990-05-15"),
            "Calle 123",
            "3102567890",
            "sbs@example.com",
            new BigDecimal("2000000")
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = buildUser();
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        StepVerifier.create(registerUserUseCase.registerUser(user)).expectNext(user).verifyComplete();
        verify(userRepository).findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserAlreadyExistsByEmailOrDocument() {
        User user = buildUser();
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.just(user));
        StepVerifier.create(registerUserUseCase.registerUser(user)).expectError(BusinessException.class).verify();
        verify(userRepository).findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFailsOnSave() {
        User user = buildUser();
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(userRepository.save(user)).thenReturn(Mono.error(new RuntimeException("DB error")));
        StepVerifier.create(registerUserUseCase.registerUser(user)).expectErrorMatches(throwable -> throwable instanceof BusinessException && throwable.getMessage().equals("Error interno al registrar usuario")).verify();
        verify(userRepository).findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber());
        verify(userRepository).save(user);
    }
}
