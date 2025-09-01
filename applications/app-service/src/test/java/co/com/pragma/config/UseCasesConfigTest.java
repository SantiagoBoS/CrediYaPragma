package co.com.pragma.config;

import co.com.pragma.model.user.gateways.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class UseCasesConfigTest {
    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(TestConfig.class)) {

            boolean useCaseBeanFound = false;
            for (String beanName : context.getBeanDefinitionNames()) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'UseCase' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class); // Mock para inyectar dependencia
        }

        @Configuration
        static class ImportUseCasesConfig {
            @Bean
            public UseCasesConfig useCasesConfig() {
                return new UseCasesConfig(); // Se importa como configuración
            }
        }
    }
}