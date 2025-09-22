package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUpdateUseCaseConfig {
    @Bean
    public LoanUpdateUseCase loanUpdateUseCase(LoanUpdateRepository loanUpdateRepository, NotificationServiceGateway notificationServiceGateway, UserRepository userRepository) {
        return new LoanUpdateUseCase(loanUpdateRepository, notificationServiceGateway, userRepository);
    }
}
