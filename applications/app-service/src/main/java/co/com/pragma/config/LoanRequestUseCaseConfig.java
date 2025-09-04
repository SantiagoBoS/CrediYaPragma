package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import co.com.pragma.usecase.loan.LoanUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanRequestUseCaseConfig {

    @Bean
    public LoanUseCase registerLoanRequestUseCase(LoanRequestRepository loanRequestRepository) {
        return new LoanUseCase(loanRequestRepository);
    }
}
