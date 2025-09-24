package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.LoanType;
import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.user.User;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoanUseCaseTest {

    private LoanRepository loanRepository;
    private UserDocumentRepository userDocumentRepository;
    private LoanTypeRepository loanTypeRepository;
    private UserRepository userRepository;
    private LoanNotificationService loanNotificationService;
    private LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;

    private LoanUseCase loanUseCase;
    private LoanRequest loanRequest;
    private LoanType loanType;
    private String token;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        userDocumentRepository = mock(UserDocumentRepository.class);
        loanTypeRepository = mock(LoanTypeRepository.class);
        userRepository = mock(UserRepository.class);
        loanNotificationService = mock(LoanNotificationService.class);
        loanCalculateCapacityUseCase = mock(LoanCalculateCapacityUseCase.class);

        loanUseCase = new LoanUseCase(
                loanRepository,
                userDocumentRepository,
                loanTypeRepository,
                loanCalculateCapacityUseCase,
                userRepository,
                loanNotificationService
        );

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
                .interestRate(1.5)
                .automaticValidation(true)
                .build();

        token = "fake-jwt-token";
    }

    @Test
    void shouldRegisterLoanWithAutomaticValidationApproved() {
        // Mock servicios
        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.just(true));
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.just(loanType));
        when(userRepository.findByDocumentNumber("12345"))
                .thenReturn(Mono.just(User.builder()
                        .email("test@mail.com")
                        .baseSalary(BigDecimal.valueOf(5000))
                        .build()));
        CapacityResult mockCapacity = mock(CapacityResult.class);
        when(mockCapacity.getDecision()).thenReturn("APPROVED");
        when(mockCapacity.getPaymentPlan()).thenReturn(List.of());
        when(loanCalculateCapacityUseCase.execute(anyString(), any(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(Mono.just(mockCapacity));
        when(loanRepository.save(any())).thenReturn(Mono.just(loanRequest));
        when(loanNotificationService.notifyApproved(any(), anyString(), anyString(), anyDouble(), anyList()))
                .thenReturn(Mono.empty());

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectNextMatches(lr -> lr.getClientDocument().equals("12345"))
                .verifyComplete();

        verify(loanNotificationService).notifyApproved(any(), anyString(), anyString(), anyDouble(), anyList());
    }

    @Test
    void shouldRegisterLoanWithoutAutomaticValidation() {
        loanType.setAutomaticValidation(false);

        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.just(true));
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.just(loanType));
        when(loanRepository.save(any())).thenReturn(Mono.just(loanRequest));

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectNextMatches(lr -> lr.getClientDocument().equals("12345"))
                .verifyComplete();

        verify(loanNotificationService, never()).notifyApproved(any(), anyString(), anyString(), anyDouble(), anyList());
        verify(loanRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenLoanTypeInvalid() {
        when(userDocumentRepository.existsByDocumentToken("12345", token)).thenReturn(Mono.just(true));
        when(loanTypeRepository.findByCode("PERSONAL")).thenReturn(Mono.empty());

        Mono<LoanRequest> result = loanUseCase.register(loanRequest, token);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof BusinessException &&
                        ex.getMessage().equals(AppMessages.LOAN_TYPE_INVALID.getMessage()))
                .verify();
    }
}
