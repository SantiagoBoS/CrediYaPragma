package co.com.pragma.config;

import co.com.pragma.model.auth.gateways.AuthRepository;
import co.com.pragma.model.auth.gateways.PasswordEncoderService;
import co.com.pragma.model.user.gateways.RoleRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class UserUseCaseConfigTest {

    @Test
    void testUserUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            UserUseCase bean = context.getBean(UserUseCase.class);
            assertNotNull(bean, "UserUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(UserUseCaseConfig.class)
    static class TestConfig {
        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        public RoleRepository roleRepository() {
            return mock(RoleRepository.class);
        }

        @Bean
        public PasswordEncoderService passwordEncoderService() {
            return mock(PasswordEncoderService.class);
        }

        @Bean
        public AuthRepository authRepository() {
            return mock(AuthRepository.class);
        }
    }
}
