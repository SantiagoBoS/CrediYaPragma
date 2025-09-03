package co.com.pragma.config;

import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.TokenProvider;
import co.com.pragma.usecase.auth.AuthUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthUseCaseConfig {
    @Bean
    public AuthUseCase authUseCase(AuthRepository authRepository, TokenProvider tokenProvider) {
        return new AuthUseCase(authRepository, tokenProvider);
    }
}
