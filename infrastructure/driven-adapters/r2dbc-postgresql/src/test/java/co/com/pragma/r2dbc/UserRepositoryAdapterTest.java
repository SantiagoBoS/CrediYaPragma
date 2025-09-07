package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.constants.AppMessages;
import co.com.pragma.model.user.exceptions.BusinessException;
import co.com.pragma.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {
    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;
    @Mock
    UserReactiveRepository repository;
    @Mock
    ObjectMapper mapper;
    @Mock
    TransactionalOperator tsOperator;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new UserRepositoryAdapter(repository, mapper, tsOperator);

        userEntity = new UserEntity();
        userEntity.setId("1");
        userEntity.setEmail("test@test.com");
        userEntity.setName("Santiago");
        userEntity.setLastName("Test");
        userEntity.setBirthDate(LocalDate.parse("1990-05-15"));
        userEntity.setAddress("Calle 3");
        userEntity.setPhone("3102000000");
        userEntity.setBaseSalary(new BigDecimal("2500000"));

        user = new User(
                "123456789", "Santiago", "Test",
                LocalDate.parse("1990-05-15"), "Calle 3",
                "3102000000", "test@test.com", new BigDecimal("2500000")
        );

        lenient().when(tsOperator.transactional(any(Flux.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(tsOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void mustFindByEmailAndDocumentNumber() {
        when(repository.findByEmailAndDocumentNumber("test@test.com", "123456789")).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        Mono<User> result = repositoryAdapter.findByEmailAndDocumentNumber("test@test.com", "123456789");
        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustSave() {
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);
        Mono<User> result = repositoryAdapter.save(user);
        StepVerifier.create(result)
            .expectNext(user)
            .verifyComplete();
    }

    @Test
    void mustThrowBusinessExceptionWhenDuplicateUser() {
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.error(new RuntimeException("duplicate email")));
        StepVerifier.create(repositoryAdapter.save(user))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    org.junit.jupiter.api.Assertions.assertEquals(
                            AppMessages.USER_ALREADY_EXISTS.getMessage(),
                            e.getMessage()
                    );
                })
                .verify();
    }

    @Test
    void mustThrowBusinessExceptionOnGeneralSaveError() {
        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(repository.save(userEntity)).thenReturn(Mono.error(new RuntimeException("database connection failed")));
        StepVerifier.create(repositoryAdapter.save(user))
                .expectErrorSatisfies(e -> {
                    assert e instanceof BusinessException;
                    org.junit.jupiter.api.Assertions.assertEquals(AppMessages.ERROR_SAVING_USER.getMessage(), e.getMessage());
                })
                .verify();
    }

    @Test
    void mustReturnEmptyWhenUserNotFound() {
        when(repository.findByEmailAndDocumentNumber("test@test.com", "123456789")).thenReturn(Mono.empty());
        Mono<User> result = repositoryAdapter.findByEmailAndDocumentNumber("test@test.com", "123456789");
        StepVerifier.create(result).verifyComplete();
    }
}
