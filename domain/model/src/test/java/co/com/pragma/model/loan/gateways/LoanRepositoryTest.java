package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanRepositoryTest {
    private LoanRepository repository;
    private final List<LoanRequest> database = new ArrayList<>();

    private LoanRequest baseRequest;
    private LocalDateTime fixedDate;

    @BeforeEach
    void setUp() {
        fixedDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        baseRequest = LoanRequest.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(fixedDate)
                .build();

        repository = new LoanRepository() {
            @Override
            public Mono<LoanRequest> save(LoanRequest loanRequest) {
                database.add(loanRequest);
                return Mono.just(loanRequest);
            }

            @Override
            public Mono<LoanRequest> findByClientDocumentAndStatus(String clientDocument, String status) {
                return Flux.fromIterable(database)
                        .filter(lr -> lr.getClientDocument().equals(clientDocument)
                                && lr.getStatus().name().equals(status))
                        .next();
            }

            @Override
            public Flux<LoanRequest> findAll() {
                return Flux.fromIterable(database);
            }
        };

        database.clear();
    }

    @Test
    void shouldSaveLoanRequest() {
        StepVerifier.create(repository.save(baseRequest))
                .expectNextMatches(saved ->
                        saved.getClientDocument().equals("12345")
                                && saved.getAmount().equals(1000.0)
                                && saved.getCreatedAt().equals(fixedDate))
                .verifyComplete();
    }

    @Test
    void shouldFindByClientDocumentAndStatus() {
        LoanRequest modified = baseRequest.toBuilder()
                .loanType(AppMessages.VALID_TYPE_LOAN_CAR.getMessage()).build();

        repository.save(modified).block();
        StepVerifier.create(repository.findByClientDocumentAndStatus("12345", RequestStatus.PENDING_REVIEW.name()))
                .expectNextMatches(found ->
                        found.getLoanType().equals(AppMessages.VALID_TYPE_LOAN_CAR.getMessage())
                                && found.getTermMonths() == 12)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        StepVerifier.create(repository.findByClientDocumentAndStatus("99999", RequestStatus.APPROVED.name())).verifyComplete();
    }

    @Test
    void shouldFindAllLoanRequests() {
        LoanRequest second = baseRequest.toBuilder()
                .clientDocument("67890").build();
        repository.save(baseRequest).block();
        repository.save(second).block();
        StepVerifier.create(repository.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }
}
