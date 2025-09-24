package co.com.pragma.usecase.loan.notification;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoanNotificationService {

    private final NotificationServiceGateway notificationServiceGateway;

    public Mono<Void> notifyApproved(LoanRequest loan, String email, String loanType, Double interestRate, List<LoanInstallment> plan) {
        return notificationServiceGateway.sendLoanStatusUpdateNotification(
                loan.getPublicId().toString(),
                loan.getStatus().toString(),
                email,
                loanType,
                loan.getAmount(),
                interestRate,
                loan.getTermMonths(),
                plan
        );
    }

    public Mono<Void> notifyRejected(LoanRequest loan, String email, String loanType) {
        return notificationServiceGateway.sendLoanStatusUpdateNotification(
                loan.getPublicId().toString(),
                loan.getStatus().toString(),
                email,
                loanType,
                null,
                null,
                null,
                List.of()
        );
    }
}
