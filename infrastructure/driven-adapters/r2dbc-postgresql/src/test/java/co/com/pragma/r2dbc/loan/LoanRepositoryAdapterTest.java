package co.com.pragma.r2dbc.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.r2dbc.loan.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class LoanRepositoryAdapterTest {
    @InjectMocks
    LoanRepositoryAdapter repositoryAdapter;

    @Mock
    LoanReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    TransactionalOperator tsOperator;

    private LoanEntity entity;
    private LoanRequest domain;

    @BeforeEach
    void setUp() {
        repositoryAdapter = new LoanRepositoryAdapter(repository, mapper, tsOperator);

        entity = new LoanEntity();
        entity.setId(Long.valueOf("1"));
        entity.setClientDocument("123456");
        entity.setAmount(10000.0);
        entity.setTermMonths(12);
        entity.setLoanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage());
        entity.setStatus(RequestStatus.PENDING_REVIEW);
        entity.setCreatedAt(LocalDateTime.now());

        domain = LoanRequest.builder()
                .clientDocument("123456")
                .amount(10000.0)
                .termMonths(12)
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(entity.getCreatedAt())
                .build();

        // Simular comportamiento del TransactionalOperator
        lenient().when(tsOperator.transactional(any(Flux.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(tsOperator.transactional(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void mustFindAllLoanRequests() {
        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, LoanRequest.class)).thenReturn(domain);
        Flux<LoanRequest> result = repositoryAdapter.findAll();
        StepVerifier.create(result)
                .expectNextMatches(loanRequest ->
                        loanRequest.getClientDocument().equals("123456") &&
                                loanRequest.getAmount().equals(10000.0) &&
                                loanRequest.getStatus() == RequestStatus.PENDING_REVIEW
                ).verifyComplete();
    }

    @Test
    void mustSaveLoanSuccessfully() {
        when(mapper.map(domain, LoanEntity.class)).thenReturn(entity);
        when(mapper.map(entity, LoanRequest.class)).thenReturn(domain);
        when(repository.save(any())).thenReturn(Mono.just(entity));
        Mono<LoanRequest> result = repositoryAdapter.save(domain);
        StepVerifier.create(result)
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void mustThrowDuplicateApplicationWhenErrorContainsDuplicate() {
        when(repository.save(any())).thenReturn(Mono.error(new RuntimeException("duplicate key")));
        Mono<LoanRequest> result = repositoryAdapter.save(domain);
        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof BusinessException &&
                        ex.getMessage().equals(AppMessages.LOAN_DUPLICATE_APPLICATION.getMessage()))
                .verify();
    }

    @Test
    void mustThrowInternalErrorWhenUnexpectedErrorOccurs() {
        when(repository.save(any())).thenReturn(Mono.error(new RuntimeException("some other error")));
        Mono<LoanRequest> result = repositoryAdapter.save(domain);
        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof BusinessException &&
                        ex.getMessage().equals(AppMessages.LOAN_INTERNAL_ERROR.getMessage()))
                .verify();
    }
}