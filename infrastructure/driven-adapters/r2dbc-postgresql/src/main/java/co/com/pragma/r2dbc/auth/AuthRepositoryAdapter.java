package co.com.pragma.r2dbc.auth;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.r2dbc.auth.entity.AuthEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class AuthRepositoryAdapter extends ReactiveAdapterOperations<Auth, AuthEntity, String, AuthReactiveRepository> implements AuthRepository {
    private final AuthReactiveRepository repository;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public AuthRepositoryAdapter(AuthReactiveRepository repository, ObjectMapper mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper, d -> mapper.map(d, Auth.class));
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Auth> save(Auth auth) {
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        return super.save(auth)
                .onErrorResume(e -> Mono.error(new BusinessException(AppMessages.AUTHENTICATION_ERROR.getMessage() + e.getMessage())));
    }

    public Mono<Auth> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toEntity);
        //repository.findByEmail(email).map(entity -> mapper.map(entity, Auth.class));
    }
}
