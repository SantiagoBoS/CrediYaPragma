package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.capacity.CapacityResult;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface LoanCapacityCalculatorService {
    Mono<CapacityResult> calculateCapacity(
            String userId,
            BigDecimal income,
            Double loanAmount,
            Double annualInterestRate,
            Integer termInMonths
    );
}