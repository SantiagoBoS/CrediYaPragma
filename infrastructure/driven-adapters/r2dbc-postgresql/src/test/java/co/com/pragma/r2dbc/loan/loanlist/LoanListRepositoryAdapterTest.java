package co.com.pragma.r2dbc.loan.loanlist;

import co.com.pragma.model.loan.loanlist.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loanlist.LoanListReactiveRepository;
import co.com.pragma.r2dbc.loan.loanlist.LoanListRepositoryAdapter;
import co.com.pragma.r2dbc.loan.loanlist.entity.LoanListEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class LoanListRepositoryAdapterTest {

    private LoanListRepositoryAdapter repositoryAdapter;

    @Mock
    private LoanListReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private LoanListEntity loanEntity1;
    private LoanListEntity loanEntity2;
    private LoanList loan1;
    private LoanList loan2;

    @BeforeEach
    void setUp() {
        repository = mock(LoanListReactiveRepository.class);
        mapper = mock(ObjectMapper.class);

        repositoryAdapter = new LoanListRepositoryAdapter(repository, mapper);

        loanEntity1 = new LoanListEntity("1", BigDecimal.valueOf(1000), 12, "test@example.com", "Santiago", "Test", "PERSONAL", BigDecimal.valueOf(10), "PENDING_REVIEW", BigDecimal.valueOf(3000), BigDecimal.valueOf(200));
        loanEntity2 = new LoanListEntity("2", BigDecimal.valueOf(2000), 24, "example@example.com", "Jose", "Test", "CAR", BigDecimal.valueOf(12), "PENDING_REVIEW", BigDecimal.valueOf(4000), BigDecimal.valueOf(300));

        loan1 = LoanList.builder()
                .amount(BigDecimal.valueOf(1000))
                .term(12)
                .email("test@example.com")
                .fullName("Santiago Test")
                .loanType("PERSONAL")
                .interestRate(BigDecimal.valueOf(10))
                .requestStatus(RequestStatus.PENDING_REVIEW)
                .baseSalary(BigDecimal.valueOf(3000))
                .monthlyPayment(BigDecimal.valueOf(200))
                .build();

        loan2 = LoanList.builder()
                .amount(BigDecimal.valueOf(2000))
                .term(24)
                .email("example@example.com")
                .fullName("Jose Test")
                .loanType("CAR")
                .interestRate(BigDecimal.valueOf(12))
                .requestStatus(RequestStatus.PENDING_REVIEW)
                .baseSalary(BigDecimal.valueOf(4000))
                .monthlyPayment(BigDecimal.valueOf(300))
                .build();

        when(mapper.map(loanEntity1, LoanList.class)).thenReturn(loan1);
        when(mapper.map(loanEntity2, LoanList.class)).thenReturn(loan2);
    }

    @Test
    void shouldFindByStatusesSuccessfully() {
        when(repository.findByRequestStatusIn(anyList())).thenReturn(Flux.just(loanEntity1, loanEntity2));
        Flux<LoanList> result = repositoryAdapter.findByStatuses(List.of(RequestStatus.PENDING_REVIEW), 0, 2);
        StepVerifier.create(result)
                .expectNextMatches(found -> found.getFullName().equals("Santiago Test") &&
                        found.getRequestStatus() == RequestStatus.PENDING_REVIEW)
                .expectNextMatches(found -> found.getFullName().equals("Jose Test") &&
                        found.getRequestStatus() == RequestStatus.PENDING_REVIEW)
                .verifyComplete();

        verify(repository).findByRequestStatusIn(anyList());
    }

    @Test
    void shouldReturnEmptyWhenNoLoansFound() {
        when(repository.findByRequestStatusIn(anyList())).thenReturn(Flux.empty());
        Flux<LoanList> result = repositoryAdapter.findByStatuses(List.of(RequestStatus.PENDING_REVIEW), 0, 2);
        StepVerifier.create(result).verifyComplete();
        verify(repository).findByRequestStatusIn(anyList());
    }

    @Test
    void shouldCountByStatusesSuccessfully() {
        when(repository.countByRequestStatusIn(anyList())).thenReturn(Mono.just(5L));
        Mono<Long> result = repositoryAdapter.countByStatuses(List.of(RequestStatus.PENDING_REVIEW));
        StepVerifier.create(result)
                .expectNext(5L)
                .verifyComplete();
        verify(repository).countByRequestStatusIn(anyList());
    }

    @Test
    void shouldReturnZeroWhenNoLoansToCount() {
        when(repository.countByRequestStatusIn(anyList())).thenReturn(Mono.just(0L));
        Mono<Long> result = repositoryAdapter.countByStatuses(List.of(RequestStatus.PENDING_REVIEW));
        StepVerifier.create(result)
                .expectNext(0L)
                .verifyComplete();
        verify(repository).countByRequestStatusIn(anyList());
    }
}