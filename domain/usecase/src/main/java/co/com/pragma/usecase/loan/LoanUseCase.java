package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.loan.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {
    private final LoanRepository loanRepository;
    private final UserGateway userGateway;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanRequest> register(LoanRequest loanRequest, String token) {
        return userGateway.existsByDocumentToken(loanRequest.getClientDocument(), token)
            .then(loanTypeRepository.findByCode(loanRequest.getLoanType())
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.LOAN_TYPE_INVALID.getMessage())))
                .then(loanRepository.save(loanRequest))
            );
    }
}
