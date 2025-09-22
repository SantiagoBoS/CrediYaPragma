package co.com.pragma.r2dbc.loan.loanlist;

import co.com.pragma.r2dbc.loan.loanlist.mapper.LoanListMapper;
import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanListRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.loan.loanlist.entity.LoanListEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class LoanListRepositoryAdapter extends ReactiveAdapterOperations<LoanList, LoanListEntity, String, LoanListReactiveRepository> implements LoanListRepository {
    private final LoanListReactiveRepository repository;

    protected LoanListRepositoryAdapter(LoanListReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanList.class));
        this.repository = repository;
    }

    @Override
    public Flux<LoanList> findByStatuses(List<RequestStatus> statuses, int page, int size) {
        var statusNames = statuses.stream().map(Enum::name).toList();
        return repository.findByRequestStatusIn(statusNames)
                .skip((long) page * size)
                .take(size)
                .map(LoanListMapper::toDomain);
    }

    @Override
    public Mono<Long> countByStatuses(List<RequestStatus> statuses) {
        var statusNames = statuses.stream().map(Enum::name).toList();
        return repository.countByRequestStatusIn(statusNames);
    }
}
