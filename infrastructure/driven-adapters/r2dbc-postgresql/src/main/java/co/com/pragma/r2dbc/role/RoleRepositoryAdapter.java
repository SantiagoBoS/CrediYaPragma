package co.com.pragma.r2dbc.role;

import co.com.pragma.model.user.RoleType;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.role.entity.RoleEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class RoleRepositoryAdapter extends ReactiveAdapterOperations<RoleType, RoleEntity, Long, RoleReactiveRepository> implements RoleRepository {

    private final RoleReactiveRepository repository;

    public RoleRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, RoleType.class));
        this.repository = repository;
    }

    @Override
    public Mono<RoleType> findByCode(String code) {
        return repository.findByCode(code).map(this::toEntity);
    }
}
