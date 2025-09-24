package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanCapacityCalculatorService;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class LoanCalculateCapacityUseCaseConfigTest {

    @Test
    void testLoanCalculateCapacityUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            LoanCalculateCapacityUseCase bean = context.getBean(LoanCalculateCapacityUseCase.class);
            assertNotNull(bean, "LoanCalculateCapacityUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(LoanCalculateCapacityUseCaseConfig.class)
    static class TestConfig {

        @Bean
        public LoanCapacityCalculatorService loanCapacityCalculatorService() {
            return mock(LoanCapacityCalculatorService.class);
        }
    }
}
