package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class LoanUseCaseConfigTest {
    @Test
    void testLoanUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            Object bean = context.getBean("loanUseCase");
            assertNotNull(bean, "LoanUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(LoanUseCaseConfig.class)
    static class TestConfig {
        @Bean
        public LoanRepository loanRepository() {
            return mock(LoanRepository.class);
        }

        @Bean
        public UserDocumentRepository userGateway() {
            return mock(UserDocumentRepository.class);
        }

        @Bean
        public LoanTypeRepository loanTypeRepository() {
            return mock(LoanTypeRepository.class);
        }
    }
}
