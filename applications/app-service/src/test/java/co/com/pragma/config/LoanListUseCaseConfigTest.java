package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanListRepository;
import co.com.pragma.usecase.loan.LoanListUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class LoanListUseCaseConfigTest {

    @Test
    void testLoanListUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            LoanListUseCase bean = context.getBean(LoanListUseCase.class);
            assertNotNull(bean, "LoanListUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(LoanListUseCaseConfig.class)
    static class TestConfig {

        @Bean
        public LoanListRepository loanListRepository() {
            return mock(LoanListRepository.class);
        }
    }
}
