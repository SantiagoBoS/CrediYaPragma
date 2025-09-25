package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoanUpdateUseCaseTest {

    private LoanUpdateRepository loanUpdateRepository;
    private LoanNotificationService loanNotificationService;
    private LoanTypeRepository loanTypeRepository;
    private UserRepository userRepository;
    private LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;
    private ReportRepository reportRepository;
    private LoanUpdateUseCase useCase;
    private UUID publicId;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        loanUpdateRepository = mock(LoanUpdateRepository.class);
        loanNotificationService = mock(LoanNotificationService.class);
        userRepository = mock(UserRepository.class);
        loanCalculateCapacityUseCase = mock(LoanCalculateCapacityUseCase.class);
        loanTypeRepository = mock(LoanTypeRepository.class);
        reportRepository = mock(ReportRepository.class);

        useCase = new LoanUpdateUseCase(
                loanUpdateRepository,
                loanTypeRepository,
                loanNotificationService,
                userRepository,
                loanCalculateCapacityUseCase,
                reportRepository
        );

        publicId = UUID.randomUUID();

        loanRequest = LoanRequest.builder()
                .publicId(publicId)
                .clientDocument("CC123")
                .loanType("PERSONAL")
                .status(RequestStatus.PENDING_REVIEW)
                .amount(1000000.0)
                .termMonths(12)
                .advisorId("advisor-1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldNotSendNotificationWhenStatusIsPending() {
        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Mono.just(loanRequest));

        Mono<LoanRequest> result = useCase.updateLoanStatus(publicId.toString(), "PENDING_REVIEW", "advisor-1");

        StepVerifier.create(result)
                .expectNext(loanRequest)
                .verifyComplete();

        verify(loanNotificationService, never())
                .notifyApproved(any(), anyString(), anyString(), anyDouble(), anyList());
    }

    @Test
    void shouldReturnErrorWhenUserNotFound() {
        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString()))
                .thenAnswer(invocation -> Mono.just(loanRequest));
        when(userRepository.findByDocumentNumber("CC123")).thenReturn(Mono.empty());

        Mono<LoanRequest> result = useCase.updateLoanStatus(publicId.toString(), "APPROVED", "advisor-1");

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof RuntimeException
                        && e.getMessage().contains("LOAN_EMAIL_NOT_FOUND"))
                .verify();

        verify(loanNotificationService, never()).notifyApproved(any(), anyString(), anyString(), anyDouble(), anyList());
    }
}
