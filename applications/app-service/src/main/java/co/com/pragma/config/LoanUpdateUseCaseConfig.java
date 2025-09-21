package co.com.pragma.config;

import co.com.pragma.model.loan.loanupdate.gateways.LoanUpdateRepository;
import co.com.pragma.usecase.loan.LoanUpdateUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUpdateUseCaseConfig {
    @Bean
    public LoanUpdateUseCase loanUpdateUseCase(LoanUpdateRepository loanUpdateRepository) {
        return new LoanUpdateUseCase(loanUpdateRepository);
    }
}
