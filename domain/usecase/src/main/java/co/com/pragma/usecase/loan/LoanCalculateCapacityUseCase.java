package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.model.loan.gateways.LoanCapacityCalculatorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class LoanCalculateCapacityUseCase {

    private final LoanCapacityCalculatorService loanCapacityCalculatorService;

    public Mono<CapacityResult> execute(
            String userId,
            BigDecimal income,
            Double loanAmount,
            Double annualInterestRate,
            Integer termInMonths
    ) {
        return loanCapacityCalculatorService.calculateCapacity(
                userId, income, loanAmount, annualInterestRate, termInMonths
        );
    }
}
