package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import co.com.pragma.usecase.loan.LoanUseCase;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUseCaseConfig {

    @Bean
    public LoanUseCase loanUseCase(
            LoanRepository loanRepository,
            UserDocumentRepository userDocumentRepository,
            LoanTypeRepository loanTypeRepository,
            LoanCalculateCapacityUseCase loanCalculateCapacityUseCase,
            UserRepository userRepository,
            LoanNotificationService loanNotificationService,
            ReportRepository reportRepository
    ) {
        return new LoanUseCase(
                loanRepository,
                userDocumentRepository,
                loanTypeRepository,
                loanCalculateCapacityUseCase,
                userRepository,
                loanNotificationService,
                reportRepository
        );
    }
}
