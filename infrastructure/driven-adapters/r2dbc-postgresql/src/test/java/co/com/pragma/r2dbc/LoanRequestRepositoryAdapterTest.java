package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.entity.LoanRequestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanRequestRepositoryAdapterTest {
    @InjectMocks
    LoanRequestRepositoryAdapter repositoryAdapter;

    @Mock
    LoanRequestReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    private LoanRequestEntity entity;
    private LoanRequest domain;

    @BeforeEach
    void setUp() {
        entity = new LoanRequestEntity();
        entity.setId("1");
        entity.setClientDocument("123456");
        entity.setAmount(10000.0);
        entity.setTermMonths(12);
        entity.setLoanType("PERSONAL");
        entity.setStatus("PENDING_REVIEW");
        entity.setCreatedAt(LocalDateTime.now());

        domain = LoanRequest.builder()
                .clientDocument("123456")
                .amount(10000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    @Test
    void mustFindByClientDocumentAndStatus() {
        when(repository.findByClientDocumentAndStatus("123456", "PENDING")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, LoanRequest.class)).thenReturn(domain);
        Mono<LoanRequest> result = repositoryAdapter.findByClientDocumentAndStatus("123456", "PENDING");
        StepVerifier.create(result).expectNext(domain).verifyComplete();
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
                )
                .verifyComplete();
    }
}
