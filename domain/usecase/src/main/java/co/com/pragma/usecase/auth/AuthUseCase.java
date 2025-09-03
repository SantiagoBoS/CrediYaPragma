package co.com.pragma.usecase.auth;

import co.com.pragma.model.auth.exception.BusinessException;
import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.TokenProvider;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;

    public Mono<String> login(String email, String password) {
        return authRepository.findByEmail(email).switchIfEmpty(
                Mono.error(new BusinessException("Usuario no encontrado")))
                .flatMap(user -> {
                    if (!user.getPassword().equals(password)) {
                        return Mono.error(new BusinessException("Credenciales inválidas"));
                    }
                    return Mono.just(tokenProvider.generateToken(user));
                })
                .onErrorResume(e -> Mono.error(new BusinessException("Error durante la autenticación: " + e.getMessage())));
    }
}
