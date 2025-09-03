package co.com.pragma.usecase.auth;

import co.com.pragma.model.auth.exception.BusinessException;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;

    public Mono<String> login(String email, String password) {
        log.info("AuthUseCase - Iniciando autenticación para email{}", email);
        return authRepository.findByEmail(email).switchIfEmpty(Mono.defer(() -> {
                    log.warn("AuthUseCase - Usuario no encontrado con email{}", email);
                    return Mono.error(new BusinessException("Usuario no encontrado"));
                }))
                .flatMap(user -> {
                    log.debug("AuthUseCase - Usuario encontrado con email={}", email);
                    if (!user.getPassword().equals(password)) {
                        log.warn("AuthUseCase - Credenciales inválidas para email={}", email);
                        return Mono.error(new BusinessException("Credenciales inválidas"));
                    }
                    log.info("AuthUseCase - Credenciales correctas, generando token para email={}", email);
                    return Mono.just(tokenProvider.generateToken(user));
                })
                .doOnSuccess(token -> log.debug("AuthUseCase - Token generado correctamente para email={}", email))
                .doOnError(e -> log.error("AuthUseCase - Error en login para email={}: {}", email, e.getMessage(), e));
    }
}
