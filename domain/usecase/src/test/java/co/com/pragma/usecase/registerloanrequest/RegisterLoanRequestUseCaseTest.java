package co.com.pragma.usecase.registerloanrequest;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.RequestStatus;
import co.com.pragma.model.loan.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterLoanRequestUseCaseTest {
    @Mock
    private LoanRequestRepository loanRequestRepository;

    @InjectMocks
    private RegisterLoanRequestUseCase registerLoanRequestUseCase;

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
        //Verificar que guarda correctamente y retorna el estado PENDING_REVIEW
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRequestRepository.save(any())).thenAnswer(invocation -> {LoanRequest saved = invocation.getArgument(0);return Mono.just(saved.toBuilder().status(RequestStatus.PENDING_REVIEW).build());});
        Mono<LoanRequest> result = registerLoanRequestUseCase.registerLoanRequest(loanRequest);
        StepVerifier.create(result).expectNextMatches(saved -> saved.getStatus() == RequestStatus.PENDING_REVIEW && saved.getClientDocument().equals("12345")).verifyComplete();
        verify(loanRequestRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenExistingLoanRequestPending() {
        //Verificar que lanza excepción si ya existe una solicitud pendiente
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.just(loanRequest));
        Mono<LoanRequest> result = registerLoanRequestUseCase.registerLoanRequest(loanRequest);
        StepVerifier.create(result).expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().contains("ya tiene una solicitud en proceso")).verify();
        verify(loanRequestRepository, never()).save(any());
    }

    @Test
    void shouldThrowDuplicateExceptionFromRepository() {
        //Verificar que lanza excepción personalizada si el repositorio lanza una excepción de clave duplicada
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRequestRepository.save(any())).thenReturn(Mono.error(new RuntimeException("duplicate key value")));
        Mono<LoanRequest> result = registerLoanRequestUseCase.registerLoanRequest(loanRequest);
        StepVerifier.create(result).expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().contains("duplicada")).verify();
    }

    @Test
    void shouldThrowGenericBusinessExceptionOnUnexpectedError() {
        //Verificar que lanza una excepción genérica si el repositorio lanza una excepción inesperada
        when(loanRequestRepository.findByClientDocumentAndStatus(any(), any())).thenReturn(Mono.empty());
        when(loanRequestRepository.save(any())).thenReturn(Mono.error(new RuntimeException("connection timeout")));
        Mono<LoanRequest> result = registerLoanRequestUseCase.registerLoanRequest(loanRequest);
        StepVerifier.create(result).expectErrorMatches(ex -> ex instanceof BusinessException && ex.getMessage().contains("Error interno")).verify();
    }

    @Test
    void shouldReturnAllLoanRequests() {
        //Verificar que retorna todas las solicitudes de préstamo
        when(loanRequestRepository.findAll()).thenReturn(Flux.just(loanRequest));
        Flux<LoanRequest> result = registerLoanRequestUseCase.getAllLoanRequests();
        StepVerifier.create(result).expectNextMatches(req -> req.getClientDocument().equals("12345")).verifyComplete();
        verify(loanRequestRepository).findAll();
    }
}