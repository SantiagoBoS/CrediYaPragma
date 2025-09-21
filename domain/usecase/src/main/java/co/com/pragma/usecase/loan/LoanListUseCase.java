package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.loanlist.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.loanlist.gateways.LoanListRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoanListUseCase {

    private final LoanListRepository repository;
    private final List<RequestStatus> listStatus= List.of(RequestStatus.PENDING_REVIEW, RequestStatus.REJECTED, RequestStatus.MANUAL_REVIEW);

    public Flux<LoanList> list(List<RequestStatus> statuses, int page, int size) {
        List<RequestStatus> finalStatuses = (statuses == null || statuses.isEmpty())
                ? listStatus
                : statuses;

        return repository.findByStatuses(finalStatuses, page, size);
    }

    public Mono<Long> countByStatuses(List<RequestStatus> statuses) {
        List<RequestStatus> finalStatuses = (statuses == null || statuses.isEmpty())
                ? listStatus
                : statuses;

        return repository.countByStatuses(finalStatuses);
    }
}
