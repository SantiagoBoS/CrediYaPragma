package co.com.pragma.config;

import co.com.pragma.model.loan.gateways.LoanCapacityCalculatorService;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanCalculateCapacityUseCaseConfig {
    @Bean
    public LoanCalculateCapacityUseCase loanCalculateCapacityUseCase(
            LoanCapacityCalculatorService loanCapacityCalculatorService
    ) {
        return new LoanCalculateCapacityUseCase(
                loanCapacityCalculatorService
        );
    }
}
