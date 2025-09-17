package co.com.pragma.r2dbc.loanlist;

import co.com.pragma.r2dbc.loanlist.mapper.LoanListMapper;
import co.com.pragma.model.loan.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanListRepository;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import co.com.pragma.r2dbc.loanlist.entity.LoanListEntity;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class LoanListRepositoryAdapter extends ReactiveAdapterOperations<LoanList, LoanListEntity, String, LoanListReactiveRepository> implements LoanListRepository {
    private final LoanListReactiveRepository repository;
    private final TransactionalOperator tsOperator;

    protected LoanListRepositoryAdapter(LoanListReactiveRepository repository, ObjectMapper mapper, TransactionalOperator tsOperator) {
        super(repository, mapper, d -> mapper.map(d, LoanList.class));
        this.repository = repository;
        this.tsOperator = tsOperator;
    }

    @Override
    public Flux<LoanList> findByStatuses(List<RequestStatus> statuses, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        return repository.findByRequestStatusIn(statuses, pageable)
                .map(LoanListMapper::toDomain)
                .as(tsOperator::transactional);
    }
}
