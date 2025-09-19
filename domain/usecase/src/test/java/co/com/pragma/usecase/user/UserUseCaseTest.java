package co.com.pragma.usecase.user;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.user.RoleType;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoderService passwordEncoderService;
    private AuthRepository authRepository;
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        passwordEncoderService = Mockito.mock(PasswordEncoderService.class);
        authRepository = Mockito.mock(AuthRepository.class);

        userUseCase = new UserUseCase(userRepository, roleRepository, passwordEncoderService, authRepository);
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
                new BigDecimal("2000000"),
                "securePassword",
                "ADMIN"
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        //Valida el flujo exitoso de registro de usuario
        User user = buildUser();
        String encodedPassword = "encodedPass";
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByCode(user.getRole())).thenReturn(Mono.just(new RoleType(1L, "ADMIN", "Administrador")));
        when(passwordEncoderService.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            return Mono.just(saved);
        });
        when(authRepository.save(any(Auth.class))).thenReturn(Mono.just(new Auth()));

        StepVerifier.create(userUseCase.registerUser(user))
                .expectNextMatches(savedUser -> savedUser.getPassword().equals(encodedPassword) && savedUser.getEmail().equals(user.getEmail()))
                .verifyComplete();

        verify(userRepository).save(any(User.class));
        verify(authRepository).save(any(Auth.class));
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserAlreadyExists() {
        // Valida que la excepción se lance si el usuario ya existe
        User user = buildUser();
        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.just(user));
        when(roleRepository.findByCode(any())).thenReturn(Mono.just(new co.com.pragma.model.user.RoleType()));

        StepVerifier.create(userUseCase.registerUser(user))
                .expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().equals(AppMessages.USER_ALREADY_EXISTS.getMessage()))
                .verify();

        verify(userRepository, never()).save(any());
        verify(authRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessExceptionWhenRoleNotFound() {
        //Valida que la excepción se lance si el rol no existe
        User user = buildUser();

        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByCode(user.getRole())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.registerUser(user))
                .expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().equals(AppMessages.ROLE_NOT_FOUND.getMessage()))
                .verify();

        verify(userRepository, never()).save(any());
        verify(authRepository, never()).save(any());
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFailsOnSave() {
        //Valida que la excepción se propague si el repositorio de usuarios falla al guardar
        User user = buildUser();
        String encodedPassword = "encodedPass";

        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByCode(user.getRole())).thenReturn(Mono.just(new RoleType(1L, "ADMIN", "Administrador")));
        when(passwordEncoderService.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(userUseCase.registerUser(user))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("DB error"))
                .verify();

        verify(authRepository, never()).save(any());
    }

    @Test
    void shouldPropagateErrorWhenAuthRepositoryFails() {
        //Valida que la excepción se propague si el repositorio de autenticación falla al guardar
        User user = buildUser();
        String encodedPassword = "encodedPass";

        when(userRepository.findByEmailAndDocumentNumber(user.getEmail(), user.getDocumentNumber())).thenReturn(Mono.empty());
        when(roleRepository.findByCode(user.getRole())).thenReturn(Mono.just(new RoleType(1L, "ADMIN", "Administrador")));
        when(passwordEncoderService.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(authRepository.save(any(Auth.class))).thenReturn(Mono.error(new RuntimeException("Auth save failed")));

        StepVerifier.create(userUseCase.registerUser(user))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Auth save failed"))
                .verify();
    }
}
