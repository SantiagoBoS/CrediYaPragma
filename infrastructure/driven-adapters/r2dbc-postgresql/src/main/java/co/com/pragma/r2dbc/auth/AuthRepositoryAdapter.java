package co.com.pragma.r2dbc.auth;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.r2dbc.auth.entity.AuthEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
public class AuthRepositoryAdapter extends ReactiveAdapterOperations<Auth, AuthEntity, String, AuthReactiveRepository> implements AuthRepository {
    private final AuthReactiveRepository repository;
    private final ObjectMapper mapper;
    private final TransactionalOperator tsOperator;

    public AuthRepositoryAdapter(AuthReactiveRepository repository, ObjectMapper mapper, TransactionalOperator tsOperator) {
        super(repository, mapper, d -> mapper.map(d, Auth.class));
        this.repository = repository;
        this.mapper = mapper;
        this.tsOperator = tsOperator;
    }

    @Override
    public Mono<Auth> save(Auth auth) {
        return super.save(auth)
            .as(tsOperator::transactional)
            .onErrorResume(e -> Mono.error(new BusinessException(AppMessages.AUTHENTICATION_ERROR.getMessage() + e.getMessage())));
    }

    public Mono<Auth> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toEntity);
    }
}
