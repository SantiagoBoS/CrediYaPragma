package co.com.pragma.r2dbc.auth;

import co.com.pragma.r2dbc.auth.entity.AuthEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AuthReactiveRepository extends ReactiveCrudRepository<AuthEntity, String>, ReactiveQueryByExampleExecutor<AuthEntity> {
    Mono<AuthEntity> findByEmail(String email);
}
