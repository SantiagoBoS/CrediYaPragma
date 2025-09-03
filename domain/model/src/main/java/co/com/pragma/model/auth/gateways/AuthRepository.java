package co.com.pragma.model.auth.gateways;

import co.com.pragma.model.auth.Auth;
import reactor.core.publisher.Mono;

public interface AuthRepository {
    Mono<Auth> findByEmail(String email);
}
