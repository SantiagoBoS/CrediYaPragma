package co.com.pragma.usecase.auth;

import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import co.com.pragma.model.auth.gateways.TokenProvider;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoderService passwordEncoder;

    public Mono<String> login(String email, String rawPassword) {
        return authRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.USER_NOT_FOUND.getMessage())))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                        return Mono.error(new BusinessException(AppMessages.INVALID_CREDENTIALS.getMessage()));
                    }
                    return Mono.just(tokenProvider.generateToken(user));
                });
    }
}
