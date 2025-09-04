package co.com.pragma.usecase.registerloanrequest;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import co.com.pragma.usecase.loan.LoanUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanUseCaseTest {
    @Mock
    private LoanRequestRepository loanRequestRepository;

    @InjectMocks
    private LoanUseCase loanUseCase;

    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanRequest = LoanRequest.builder()
                .clientDocument("12345")
                .amount(10000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldRegisterLoanRequestSuccessfully() {
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRequestRepository.save(any())).thenAnswer(invocation -> {LoanRequest saved = invocation.getArgument(0);return Mono.just(saved.toBuilder().status(RequestStatus.PENDING_REVIEW).build());});
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result).expectNextMatches(saved -> saved.getStatus() == RequestStatus.PENDING_REVIEW && saved.getClientDocument().equals("12345")).verifyComplete();
        verify(loanRequestRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenExistingLoanRequestPending() {
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.just(loanRequest));
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result).expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().contains("Ya tiene una solicitud en proceso")).verify();
        verify(loanRequestRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateExceptionFromRepository() {
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRequestRepository.save(any())).thenReturn(Mono.error(new BusinessException("Ya existe una solicitud duplicada para este cliente")));
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result).expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().contains("duplicada")).verify();
    }

    @Test
    void shouldThrowGenericBusinessExceptionOnUnexpectedError() {
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRequestRepository.save(any())).thenReturn(Mono.error(new BusinessException("Error interno al registrar solicitud")));
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result).expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().contains("Error interno")).verify();
    }
    @Test
    void shouldReturnAllLoanRequests() {
        when(loanRequestRepository.findAll()).thenReturn(Flux.just(loanRequest));
        Flux<LoanRequest> result = loanUseCase.getAllLoanRequests();
        StepVerifier.create(result).expectNextMatches(req -> req.getClientDocument().equals("12345")).verifyComplete();
        verify(loanRequestRepository).findAll();
    }
}