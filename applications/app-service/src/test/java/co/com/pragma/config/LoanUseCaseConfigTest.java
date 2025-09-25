package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import co.com.pragma.usecase.loan.LoanUseCase;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
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
            LoanUseCase bean = context.getBean(LoanUseCase.class);
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
        public UserDocumentRepository userDocumentRepository() {
            return mock(UserDocumentRepository.class);
        }

        @Bean
        public LoanTypeRepository loanTypeRepository() {
            return mock(LoanTypeRepository.class);
        }

        @Bean
        public LoanCalculateCapacityUseCase loanCalculateCapacityUseCase() {
            return mock(LoanCalculateCapacityUseCase.class);
        }

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        public LoanNotificationService loanNotificationService() {
            return mock(LoanNotificationService.class);
        }

        @Bean
        public ReportRepository reportRepository() {
            return mock(ReportRepository.class);
        }
    }
}
