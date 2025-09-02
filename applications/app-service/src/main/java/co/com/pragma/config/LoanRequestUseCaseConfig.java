package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import co.com.pragma.usecase.registerloanrequest.RegisterLoanRequestUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanRequestUseCaseConfig {

    @Bean
    public RegisterLoanRequestUseCase registerLoanRequestUseCase(LoanRequestRepository loanRequestRepository) {
        return new RegisterLoanRequestUseCase(loanRequestRepository);
    }
}
