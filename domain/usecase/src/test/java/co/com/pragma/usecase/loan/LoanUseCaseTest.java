package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.UserGateway;
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
    private LoanRepository loanRepository;

    @Mock
    private UserGateway userGateway;

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
                .loanType(AppMessages.VALID_TYPE_LOAN_PERSONAL.getMessage())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldRegisterLoanRequestSuccessfully() {
        when(userGateway.existsByDocument("12345")).thenReturn(Mono.just(true));
        when(loanRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRepository.save(any())).thenAnswer(invocation -> {
                LoanRequest saved = invocation.getArgument(0);
                return Mono.just(saved.toBuilder().status(RequestStatus.PENDING_REVIEW).build());
            });
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getStatus() == RequestStatus.PENDING_REVIEW &&
                                saved.getClientDocument().equals("12345"))
                .verifyComplete();

        verify(loanRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenExistingLoanRequestPending() {
        when(userGateway.existsByDocument("12345")).thenReturn(Mono.just(true));
        when(loanRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.just(loanRequest));
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().contains(AppMessages.LOAN_APPLICATION_IN_PROCESS.getMessage()))
                .verify();

        verify(loanRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateExceptionFromRepository() {
        when(userGateway.existsByDocument("12345")).thenReturn(Mono.just(true));
        when(loanRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRepository.save(any())).thenReturn(Mono.error(new BusinessException(AppMessages.LOAN_DUPLICATE_APPLICATION.getMessage())));
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().contains(AppMessages.LOAN_DUPLICATE_APPLICATION.getMessage()))
                .verify();
    }

    @Test
    void shouldThrowGenericBusinessExceptionOnUnexpectedError() {
        when(userGateway.existsByDocument("12345")).thenReturn(Mono.just(true));
        when(loanRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRepository.save(any())).thenReturn(Mono.error(new BusinessException(AppMessages.LOAN_INTERNAL_ERROR.getMessage())));
        Mono<LoanRequest> result = loanUseCase.register(loanRequest);
        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().contains(AppMessages.LOAN_INTERNAL_ERROR.getMessage()))
                .verify();
    }

    @Test
    void shouldReturnAllLoanRequests() {
        when(loanRepository.findAll()).thenReturn(Flux.just(loanRequest));
        Flux<LoanRequest> result = loanUseCase.getAllLoanRequests();
        StepVerifier.create(result)
                .expectNextMatches(req -> req.getClientDocument().equals("12345"))
                .verifyComplete();

        verify(loanRepository).findAll();
    }

    @Test
    void shouldStillProceedWhenUserDoesNotExist() {
        when(userGateway.existsByDocument("12345")).thenReturn(Mono.empty());
        when(loanRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRepository.save(any())).thenReturn(Mono.just(loanRequest));

        Mono<LoanRequest> result = loanUseCase.register(loanRequest);

        StepVerifier.create(result)
                .expectNextMatches(req -> req.getClientDocument().equals("12345"))
                .verifyComplete();

        verify(loanRepository).save(any());
    }
}