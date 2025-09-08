package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.User;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

class UserRepositoryTest {

    private final UserRepository repository = new UserRepository() {
        @Override
        public Mono<User> save(User user) {
            return Mono.just(user);
        }

        @Override
        public Mono<User> findByEmailAndDocumentNumber(String email, String documentNumber) {
            if (email.equals("test@example.com") && documentNumber.equals("123")) {
                return Mono.just(User.builder()
                        .documentNumber("123")
                        .name("Santiago")
                        .lastName("Test")
                        .birthDate(LocalDate.of(1995, 5, 20))
                        .address("Calle Falsa 123")
                        .phone("3001234567")
                        .email("test@example.com")
                        .baseSalary(BigDecimal.valueOf(5000))
                        .build());
            }
            return Mono.empty();
        }

        @Override
        public Mono<User> findByDocumentNumber(String documentNumber) {
            if (documentNumber.equals("123")) {
                return Mono.just(User.builder()
                        .documentNumber("123")
                        .name("Santiago")
                        .email("test@example.com")
                        .build());
            }
            return Mono.empty();
        }
    };

    @Test
    void shouldSaveUser() {
        User user = User.builder()
                .documentNumber("123")
                .name("Santiago")
                .email("test@example.com")
                .build();

        StepVerifier.create(repository.save(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void shouldFindUserByEmailAndDocumentNumber() {
        StepVerifier.create(repository.findByEmailAndDocumentNumber("test@example.com", "123"))
                .expectNextMatches(user -> user.getName().equals("Santiago"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        StepVerifier.create(repository.findByEmailAndDocumentNumber("prueba@example.com", "999")).verifyComplete();
    }

    @Test
    void shouldFindUserByDocumentNumber() {
        StepVerifier.create(repository.findByDocumentNumber("123"))
                .expectNextMatches(user -> user.getDocumentNumber().equals("123"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenDocumentNotFound() {
        StepVerifier.create(repository.findByDocumentNumber("999")).verifyComplete();
    }
}
