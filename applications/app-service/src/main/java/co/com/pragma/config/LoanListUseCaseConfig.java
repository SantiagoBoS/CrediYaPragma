package co.com.pragma.config;

import co.com.pragma.model.loan.loanlist.gateways.LoanListRepository;
import co.com.pragma.usecase.loan.LoanListUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanListUseCaseConfig {
    @Bean
    public LoanListUseCase loanListUseCase(LoanListRepository loanListRepository) {
        return new LoanListUseCase(loanListRepository);
    }
}
