package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.model.loan.loanupdate.gateways.LoanUpdateRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUpdateUseCase {
    private final LoanUpdateRepository loanUpdateRepository;

    public Mono<LoanRequest> updateLoanStatus(String publicId, String newStatus, String advisorId) {
        return loanUpdateRepository.updateStatus(publicId, newStatus, advisorId);
    }
}
