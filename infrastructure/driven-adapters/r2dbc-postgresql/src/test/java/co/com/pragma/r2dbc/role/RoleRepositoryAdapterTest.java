package co.com.pragma.r2dbc.role;

import co.com.pragma.model.user.RoleType;
import co.com.pragma.r2dbc.role.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class RoleRepositoryAdapterTest {

    private RoleRepositoryAdapter repositoryAdapter;

    @Mock
    private RoleReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private RoleEntity roleEntity;
    private RoleType roleType;

    @BeforeEach
    void setUp() {
        repository = mock(RoleReactiveRepository.class);
        mapper = mock(ObjectMapper.class);

        repositoryAdapter = new RoleRepositoryAdapter(repository, mapper);

        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setCode("ADMIN");
        roleEntity.setDescription("Administrator role");

        roleType = new RoleType();
        roleType.setId(1L);
        roleType.setCode("ADMIN");
        roleType.setDescription("Administrator role");

        when(mapper.map(roleEntity, RoleType.class)).thenReturn(roleType);
    }

    @Test
    void shouldFindByCodeSuccessfully() {
        //Verificar que cuando se encuentra el rol, el repositorio devuelve el RoleType correcto
        when(repository.findByCode("ADMIN")).thenReturn(Mono.just(roleEntity));
        Mono<RoleType> result = repositoryAdapter.findByCode("ADMIN");
        StepVerifier.create(result)
                .expectNextMatches(found -> found.getId().equals(1L) &&
                        found.getCode().equals("ADMIN") && found.getDescription().equals("Administrator role"))
                .verifyComplete();

        verify(repository).findByCode("ADMIN");
    }

    @Test
    void shouldReturnEmptyWhenRoleNotFound() {
        //Verificar que cuando no se encuentra el rol, el repositorio devuelve Mono.empty()
        when(repository.findByCode("NOT_FOUND")).thenReturn(Mono.empty());
        Mono<RoleType> result = repositoryAdapter.findByCode("NOT_FOUND");
        StepVerifier.create(result).verifyComplete();
        verify(repository).findByCode("NOT_FOUND");
    }
}
