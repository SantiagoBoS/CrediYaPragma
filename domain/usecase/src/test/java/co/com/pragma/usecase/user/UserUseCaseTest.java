package co.com.pragma.usecase.user;

import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class UserUseCaseTest {

    private UserRepository userRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userUseCase = new UserUseCase(userRepository);
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
        StepVerifier.create(userUseCase.registerUser(user)).expectNext(user).verifyComplete();
        verify(userRepository).findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserAlreadyExistsByEmailOrDocument() {
        User user = buildUser();
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.just(user));
        StepVerifier.create(userUseCase.registerUser(user)).expectError(BusinessException.class).verify();
        verify(userRepository).findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFailsOnSave() {
        User user = buildUser();
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(userRepository.save(user)).thenReturn(Mono.error(new RuntimeException("DB error")));
        StepVerifier.create(userUseCase.registerUser(user)).expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("DB error")).verify();
        verify(userRepository).findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber());
        verify(userRepository).save(user);
    }
}
