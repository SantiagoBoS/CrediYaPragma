package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.usecase.loan.LoanUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUseCaseConfig {

    @Bean
    public LoanUseCase registerLoanRequestUseCase(LoanRepository loanRepository) {
        return new LoanUseCase(loanRepository);
    }
}
