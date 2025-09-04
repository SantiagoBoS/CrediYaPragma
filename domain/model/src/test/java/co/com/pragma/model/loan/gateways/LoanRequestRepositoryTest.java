package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanRequestRepositoryTest {
    private LoanRequestRepository repository;

    private final List<LoanRequest> database = new ArrayList<>();

    @BeforeEach
    void setUp() {
        repository = new LoanRequestRepository() {
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
        LoanRequest request = LoanRequest.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        StepVerifier.create(repository.save(request)).expectNextMatches(saved -> saved.getClientDocument().equals("12345") && saved.getAmount().equals(1000.0)).verifyComplete();
    }

    @Test
    void shouldFindByClientDocumentAndStatus() {
        //Valida si encuentra la solicitud por documento y estado
        LoanRequest request = LoanRequest.builder()
                .clientDocument("12345")
                .amount(2000.0)
                .termMonths(24)
                .loanType("CAR")
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(request).block();
        StepVerifier.create(repository.findByClientDocumentAndStatus("12345", "PENDING_REVIEW"))
                .expectNextMatches(found -> found.getLoanType().equals("CAR") && found.getTermMonths() == 24)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        //Valida si no encuentra la solicitud
        StepVerifier.create(repository.findByClientDocumentAndStatus("99999", "APPROVED")).verifyComplete();
    }

    @Test
    void shouldFindAllLoanRequests() {
        //Valida si encuentra todas las solicitudes
        LoanRequest request1 = LoanRequest.builder()
                .clientDocument("12345")
                .amount(1000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .status(RequestStatus.PENDING_REVIEW)
                .createdAt(LocalDateTime.now())
                .build();

        LoanRequest request2 = LoanRequest.builder()
                .clientDocument("67890")
                .amount(5000.0)
                .termMonths(36)
                .loanType("MORTGAGE")
                .status(RequestStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(request1).block();
        repository.save(request2).block();
        StepVerifier.create(repository.findAll()).expectNextCount(2).verifyComplete();
    }
}
