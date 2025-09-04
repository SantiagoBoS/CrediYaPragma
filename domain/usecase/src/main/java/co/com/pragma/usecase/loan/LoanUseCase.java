package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {
    private final LoanRequestRepository loanRequestRepository;
    public Mono<LoanRequest> register(LoanRequest loanRequest) {
        return loanRequestRepository.findByClientDocumentAndStatus(loanRequest.getClientDocument(), RequestStatus.PENDING_REVIEW.toString())
            .flatMap(existing -> Mono.<LoanRequest>error(new BusinessException(AppMessages.APPLICATION_IN_PROCESS)))
            .switchIfEmpty(Mono.defer(() -> loanRequestRepository.save(loanRequest)));
    }

    public Flux<LoanRequest> getAllLoanRequests() {
        return loanRequestRepository.findAll();
    }
}
