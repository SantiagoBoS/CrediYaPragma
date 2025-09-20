package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.loanrequest.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.loantype.LoanType;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.loanrequest.gateways.LoanRepository;
import co.com.pragma.model.loan.loantype.gateways.LoanTypeRepository;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanUseCaseTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserDocumentRepository userDocumentRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private LoanUseCase loanUseCase;

    private LoanRequest loanRequest;
    private LoanType loanType;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loanRequest = LoanRequest.builder()
                .clientDocument("12345")
                .amount(10000.0)
                .termMonths(12)
                .loanType("PERSONAL")
                .createdAt(LocalDateTime.now())
                .status(RequestStatus.PENDING_REVIEW)
                .build();

        loanType = LoanType.builder()
                .code("PERSONAL")
                .description("Préstamo personal")
                .build();

        token = "fake-jwt-token";
    }

    @Test
    void shouldRegisterLoanRequestSuccessfully() {
        //Valida que se registre una solicitud de préstamo exitosamente
        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.just(true));
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.just(loanType));
        when(loanRepository.save(any())).thenReturn(Mono.just(loanRequest));

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectNextMatches(saved ->
                        saved.getClientDocument().equals("12345") &&
                                saved.getStatus() == RequestStatus.PENDING_REVIEW)
                .verifyComplete();

        verify(loanRepository).save(any());
    }

    @Test
    void shouldThrowDuplicateExceptionFromRepository() {
        //Valida que se lance una excepción de duplicado cuando el repositorio lo indique
        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.just(true));
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.just(loanType));
        when(loanRepository.save(any()))
                .thenReturn(Mono.error(new BusinessException(AppMessages.LOAN_DUPLICATE_APPLICATION.getMessage())));

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException &&
                                ex.getMessage().contains(AppMessages.LOAN_DUPLICATE_APPLICATION.getMessage()))
                .verify();
    }

    @Test
    void shouldThrowGenericBusinessExceptionOnUnexpectedError() {
        //Valida que se lance una excepción genérica cuando ocurre un error inesperado
        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.just(true));
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.just(loanType));
        when(loanRepository.save(any())).thenReturn(Mono.error(new BusinessException(AppMessages.LOAN_INTERNAL_ERROR.getMessage())));

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof BusinessException &&
                        ex.getMessage().contains(AppMessages.LOAN_INTERNAL_ERROR.getMessage()))
                .verify();
    }

    @Test
    void shouldProceedWhenUserGatewayReturnsEmpty() {
        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.empty());
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.just(loanType));
        when(loanRepository.save(any())).thenReturn(Mono.just(loanRequest));

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectNextMatches(req -> req.getClientDocument().equals("12345"))
                .verifyComplete();

        verify(loanRepository).save(any());
    }
}