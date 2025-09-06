package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {
    private final LoanRepository loanRepository;
    private final TransactionalOperator txOperator;

    public Mono<LoanRequest> register(LoanRequest loanRequest) {
        return loanRepository.findByClientDocumentAndStatus(loanRequest.getClientDocument(), RequestStatus.PENDING_REVIEW.toString())
            .flatMap(existing -> Mono.<LoanRequest>error(new BusinessException(AppMessages.APPLICATION_IN_PROCESS)))
            .switchIfEmpty(Mono.defer(() -> loanRepository.save(loanRequest)))
            .as(txOperator::transactional);
    }

    public Flux<LoanRequest> getAllLoanRequests() {
        return loanRepository.findAll().as(txOperator::transactional);
    }
}
