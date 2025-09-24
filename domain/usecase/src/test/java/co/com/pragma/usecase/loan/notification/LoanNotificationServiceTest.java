package co.com.pragma.usecase.loan.notification;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoanNotificationServiceTest {

    private NotificationServiceGateway notificationServiceGateway;
    private LoanNotificationService loanNotificationService;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        notificationServiceGateway = Mockito.mock(NotificationServiceGateway.class);
        loanNotificationService = new LoanNotificationService(notificationServiceGateway);

        loanRequest = LoanRequest.builder()
                .publicId(UUID.randomUUID())
                .status(RequestStatus.APPROVED)
                .amount(500000.0)
                .termMonths(12)
                .build();
    }

    @Test
    void shouldNotifyApprovedSuccessfully() {
        when(notificationServiceGateway.sendLoanStatusUpdateNotification(
                anyString(), anyString(), anyString(), anyString(),
                any(), any(), any(), anyList()))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                loanNotificationService.notifyApproved(
                        loanRequest,
                        "test@mail.com",
                        "PERSONAL",
                        1.5,
                        List.of()
                )
        ).verifyComplete();

        verify(notificationServiceGateway, times(1))
                .sendLoanStatusUpdateNotification(
                        anyString(), anyString(), anyString(), anyString(),
                        any(), any(), any(), anyList());
    }

    @Test
    void shouldNotifyRejectedSuccessfully() {
        when(notificationServiceGateway.sendLoanStatusUpdateNotification(
                anyString(), anyString(), anyString(), anyString(),
                any(), any(), any(), anyList()))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                loanNotificationService.notifyRejected(
                        loanRequest,
                        "test@mail.com",
                        "PERSONAL"
                )
        ).verifyComplete();

        verify(notificationServiceGateway, times(1))
                .sendLoanStatusUpdateNotification(
                        anyString(), anyString(), anyString(), anyString(),
                        any(), any(), any(), anyList());
    }
}
