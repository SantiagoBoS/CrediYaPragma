package co.com.pragma.usecase.registerloanrequest;

import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RegisterLoanRequestUseCase {
    private final LoanRequestRepository laonRequestRepository;
}
