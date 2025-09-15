package co.com.pragma.r2dbc.role;

import co.com.pragma.r2dbc.role.entity.RoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long>, ReactiveQueryByExampleExecutor<RoleEntity> {
    Mono<RoleEntity> findByCode(String code);
}
