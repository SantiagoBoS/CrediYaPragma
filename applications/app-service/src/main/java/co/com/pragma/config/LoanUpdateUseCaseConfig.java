package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUpdateUseCaseConfig {
    @Bean
    public LoanUpdateUseCase loanUpdateUseCase(
            LoanUpdateRepository loanUpdateRepository,
            LoanTypeRepository loanTypeRepository,
            LoanNotificationService loanNotificationService,
            UserRepository userRepository,
            LoanCalculateCapacityUseCase loanCalculateCapacityUseCase
    ) {
        return new LoanUpdateUseCase(
                loanUpdateRepository,
                loanTypeRepository,
                loanNotificationService,
                userRepository,
                loanCalculateCapacityUseCase
        );
    }
}
