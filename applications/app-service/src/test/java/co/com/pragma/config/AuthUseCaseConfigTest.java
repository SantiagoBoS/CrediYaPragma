package co.com.pragma.config;

import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.TokenProvider;
import co.com.pragma.usecase.auth.AuthUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class AuthUseCaseConfigTest {

    @Test
    void testAuthUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            AuthUseCase bean = context.getBean(AuthUseCase.class);
            assertNotNull(bean, "AuthUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(AuthUseCaseConfig.class)
    static class TestConfig {

        @Bean
        public AuthRepository authRepository() {
            return mock(AuthRepository.class); // Mock para dependencia
        }

        @Bean
        public TokenProvider tokenProvider() {
            return mock(TokenProvider.class); // Mock para dependencia
        }
    }
}