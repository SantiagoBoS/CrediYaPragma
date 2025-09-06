package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.AppMessages;
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
    LoanRequest requestTest = LoanRequest.builder()
            .clientDocument("12345")
            .amount(1000.0)
            .termMonths(12)
            .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
            .status(RequestStatus.PENDING_REVIEW)
            .createdAt(LocalDateTime.now())
            .build();

    @BeforeEach
    void setUp() {
        repository = new LoanRepository() {
            @Override
            public Mono<LoanRequest> save(LoanRequest loanRequest) {
                database.add(loanRequest);
                return Mono.just(loanRequest);
            }

            @Override
            public Mono<LoanRequest> findByClientDocumentAndStatus(String clientDocument, String status) {
                // Simula la búsqueda en la "base de datos"
                return Flux.fromIterable(database).filter(lr -> lr.getClientDocument().equals(clientDocument) && lr.getStatus().name().equals(status)).next();
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
        //Valida si guarda la solicitud
        StepVerifier.create(repository.save(requestTest))
                .expectNextMatches(saved ->
                        saved.getClientDocument().equals("12345") && saved.getAmount().equals(1000.0))
                .verifyComplete();
    }

    @Test
    void shouldFindByClientDocumentAndStatus() {
        //Valida si encuentra la solicitud por documento y estado
        requestTest.setLoanType(AppMessages.VALID_TYPE_LOAN_CAR.getMessage());
        repository.save(requestTest).block();
        StepVerifier.create(repository.findByClientDocumentAndStatus("12345", RequestStatus.PENDING_REVIEW.name()))
                .expectNextMatches(found -> found.getLoanType().equals(AppMessages.VALID_TYPE_LOAN_CAR.getMessage()) && found.getTermMonths() == 12)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        //Valida si no encuentra la solicitud
        StepVerifier.create(repository.findByClientDocumentAndStatus("99999", RequestStatus.APPROVED.name())).verifyComplete();
    }

    @Test
    void shouldFindAllLoanRequests() {
        //Valida si encuentra todas las solicitudes
        repository.save(requestTest).block();
        requestTest.setClientDocument("67890");
        repository.save(requestTest).block();
        StepVerifier.create(repository.findAll()).expectNextCount(2).verifyComplete();
    }
}
