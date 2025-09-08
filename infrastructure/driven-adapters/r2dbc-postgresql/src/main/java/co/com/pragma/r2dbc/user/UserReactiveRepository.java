package co.com.pragma.r2dbc.user;

import co.com.pragma.r2dbc.user.entity.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByEmailAndDocumentNumber(String email, String documentNumber);
    Mono<UserEntity> findByDocumentNumber(String documentNumber);
}
