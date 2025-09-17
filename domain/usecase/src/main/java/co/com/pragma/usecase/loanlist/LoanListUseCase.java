package co.com.pragma.usecase.loanlist;

import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanListRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class LoanListUseCase {

    private final LoanListRepository repository;

    public Flux<LoanList> list(List<RequestStatus> statuses, Integer page, Integer size) {
        List<RequestStatus> finalStatuses = (statuses == null || statuses.isEmpty())
                ? List.of(RequestStatus.PENDING_REVIEW, RequestStatus.REJECTED, RequestStatus.MANUAL_REVIEW)
                : statuses;

        return repository.findByStatuses(finalStatuses, page, size);
    }
}
