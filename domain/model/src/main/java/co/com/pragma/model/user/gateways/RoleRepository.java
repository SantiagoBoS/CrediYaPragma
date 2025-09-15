package co.com.pragma.model.user.gateways;

import co.com.pragma.model.user.RoleType;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<RoleType> findByCode(String code);
}
