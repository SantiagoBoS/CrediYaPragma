package co.com.pragma.r2dbc;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.r2dbc.entity.AuthEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class AuthRepositoryAdapter extends ReactiveAdapterOperations<Auth, AuthEntity, String, AuthReactiveRepository> implements AuthRepository {
    private final AuthReactiveRepository repository;
    private final ObjectMapper mapper;

    public AuthRepositoryAdapter(AuthReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Auth.class));
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<Auth> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toEntity);
        //repository.findByEmail(email).map(entity -> mapper.map(entity, Auth.class));
    }
}
