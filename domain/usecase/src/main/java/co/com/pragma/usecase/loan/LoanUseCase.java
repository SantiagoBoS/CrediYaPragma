package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUseCase {
    private final LoanRepository loanRepository;
    private final UserGateway userGateway;

    public Mono<LoanRequest> register(LoanRequest loanRequest) {
        return userGateway.existsByDocument(loanRequest.getClientDocument())
                .then(loanRepository.save(loanRequest));
    }

    public Flux<LoanRequest> getAllLoanRequests() {
        return loanRepository.findAll();
    }
}
