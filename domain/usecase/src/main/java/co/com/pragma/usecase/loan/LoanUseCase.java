package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.loanrequest.gateways.LoanRepository;
import co.com.pragma.model.loan.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {
    private final LoanRepository loanRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final LoanTypeRepository loanTypeRepository;

    public Mono<LoanRequest> register(LoanRequest loanRequest, String token) {
        return userDocumentRepository.existsByDocumentToken(loanRequest.getClientDocument(), token)
            .then(loanTypeRepository.findByCode(loanRequest.getLoanType())
                .switchIfEmpty(Mono.error(new BusinessException(AppMessages.LOAN_TYPE_INVALID.getMessage())))
                .then(loanRepository.save(loanRequest))
            );
    }
}
