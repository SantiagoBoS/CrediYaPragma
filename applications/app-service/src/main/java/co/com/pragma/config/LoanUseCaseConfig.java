package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import co.com.pragma.usecase.loan.LoanUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanUseCaseConfig {

    @Bean
    public LoanUseCase loanUseCase(LoanRepository loanRepository, UserDocumentRepository userDocumentRepository, LoanTypeRepository loanTypeRepository) {
        return new LoanUseCase(loanRepository, userDocumentRepository, loanTypeRepository);
    }
}
