package co.com.pragma.r2dbc.auth;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.r2dbc.auth.entity.AuthEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRepositoryAdapterTest {

    @InjectMocks
    private AuthRepositoryAdapter repositoryAdapter;

    @Mock
    private AuthReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private TransactionalOperator tsOperator;

    private AuthEntity authEntity;
    private Auth auth;

    @BeforeEach
    void setUp() {
        auth = Auth.builder()
                .email("test@example.com")
                .password("secret")
                .document("123")
                .role("USER")
                .build();

        authEntity = new AuthEntity();
        authEntity.setEmail(auth.getEmail());
        authEntity.setPassword(auth.getPassword());
        authEntity.setDocument(auth.getDocument());
        authEntity.setRole(auth.getRole());

        // Mock de transacciones (no hacer nada)
        lenient().when(tsOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldSaveAuthSuccessfully() {
        when(repository.save(any(AuthEntity.class))).thenReturn(Mono.just(authEntity));
        when(mapper.map(authEntity, Auth.class)).thenReturn(auth);
        when(mapper.map(auth, AuthEntity.class)).thenReturn(authEntity);

        StepVerifier.create(repositoryAdapter.save(auth))
                .expectNextMatches(saved ->
                        saved.getEmail().equals("test@example.com") &&
                                saved.getRole().equals("USER")
                )
                .verifyComplete();

        verify(repository).save(any(AuthEntity.class));
    }

    @Test
    void shouldHandleSaveError() {
        when(repository.save(any(AuthEntity.class))).thenReturn(Mono.error(new RuntimeException("DB error")));
        when(mapper.map(auth, AuthEntity.class)).thenReturn(authEntity);

        StepVerifier.create(repositoryAdapter.save(auth))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().contains(AppMessages.AUTHENTICATION_ERROR.getMessage())
                )
                .verify();

        verify(repository).save(any(AuthEntity.class));
    }

    @Test
    void shouldFindByEmail() {
        when(repository.findByEmail("test@example.com")).thenReturn(Mono.just(authEntity));
        when(mapper.map(authEntity, Auth.class)).thenReturn(auth);

        StepVerifier.create(repositoryAdapter.findByEmail("test@example.com"))
                .expectNextMatches(found ->
                        found.getEmail().equals("test@example.com") &&
                                found.getRole().equals("USER")
                )
                .verifyComplete();

        verify(repository).findByEmail("test@example.com");
    }
}