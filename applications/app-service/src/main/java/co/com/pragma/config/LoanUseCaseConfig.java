package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.UserGateway;
import co.com.pragma.usecase.loan.LoanUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUseCaseConfig {

    @Bean
    public LoanUseCase loanUseCase(LoanRepository loanRepository, UserGateway userGateway) {
        return new LoanUseCase(loanRepository, userGateway);
    }
}
