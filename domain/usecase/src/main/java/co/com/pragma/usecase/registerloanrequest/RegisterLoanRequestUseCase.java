package co.com.pragma.usecase.registerloanrequest;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterLoanRequestUseCase {
    private final LoanRequestRepository loanRequestRepository;

    public Mono<LoanRequest> registerLoanRequest(LoanRequest loanRequest) {
        return loanRequestRepository.findByClientDocumentAndStatus(loanRequest.getClientDocument(), RequestStatus.PENDING_REVIEW.toString())
            .flatMap(existing -> Mono.<LoanRequest>error(new BusinessException("El cliente ya tiene una solicitud en proceso")))
            .switchIfEmpty(Mono.defer(() -> loanRequestRepository.save(loanRequest).onErrorResume(throwable -> {
                String msg = throwable.getMessage();
                if (msg != null && msg.contains("duplicate")) {
                    return Mono.error(new BusinessException("Ya existe una solicitud duplicada para este cliente"));
                }
                return Mono.error(new BusinessException("Error interno al registrar solicitud"));
            })));
    }
}
