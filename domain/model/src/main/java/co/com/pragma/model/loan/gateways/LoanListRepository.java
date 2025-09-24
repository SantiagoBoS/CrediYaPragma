package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoanListRepository {
    Flux<LoanList> findByStatuses(List<RequestStatus> statuses, Integer page, Integer size);
    Mono<Long> countByStatuses(List<RequestStatus> statuses);
}