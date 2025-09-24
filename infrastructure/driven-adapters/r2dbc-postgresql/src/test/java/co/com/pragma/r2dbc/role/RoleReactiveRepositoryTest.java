package co.com.pragma.r2dbc.role;

import co.com.pragma.r2dbc.role.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RoleReactiveRepositoryTest {

    private RoleReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(RoleReactiveRepository.class);
    }

    @Test
    void shouldFindByCodeSuccessfully() {
        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setCode("ADMIN");
        role.setDescription("Administrator role");

        // Mockeamos la llamada
        when(repository.findByCode("ADMIN")).thenReturn(Mono.just(role));

        StepVerifier.create(repository.findByCode("ADMIN"))
                .expectNextMatches(found ->
                        found.getId().equals(1L) &&
                                found.getCode().equals("ADMIN") &&
                                found.getDescription().equals("Administrator role")
                )
                .verifyComplete();

        verify(repository, times(1)).findByCode("ADMIN");
    }

    @Test
    void shouldReturnEmptyWhenCodeNotFound() {
        // Mockeamos que no se encuentra
        when(repository.findByCode("NOT_FOUND")).thenReturn(Mono.empty());

        StepVerifier.create(repository.findByCode("NOT_FOUND"))
                .verifyComplete();

        verify(repository, times(1)).findByCode("NOT_FOUND");
    }
}
