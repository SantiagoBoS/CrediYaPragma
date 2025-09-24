package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class LoanUpdateUseCaseConfigTest {

    @Test
    void testLoanUpdateUseCaseBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            LoanUpdateUseCase bean = context.getBean(LoanUpdateUseCase.class);
            assertNotNull(bean, "LoanUpdateUseCase bean should exist in the context");
        }
    }

    @Configuration
    @Import(LoanUpdateUseCaseConfig.class)
    static class TestConfig {

        @Bean
        public LoanUpdateRepository loanUpdateRepository() {
            return mock(LoanUpdateRepository.class);
        }

        @Bean
        public LoanTypeRepository loanTypeRepository() {
            return mock(LoanTypeRepository.class);
        }

        @Bean
        public LoanNotificationService loanNotificationService() {
            return mock(LoanNotificationService.class);
        }

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        public LoanCalculateCapacityUseCase loanCalculateCapacityUseCase() {
            return mock(LoanCalculateCapacityUseCase.class);
        }
    }
}
