package co.com.pragma.model.sqsnotification.gateways;

import co.com.pragma.model.loan.capacity.LoanInstallment;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NotificationServiceGateway {
    Mono<Void> sendLoanStatusUpdateNotification(
            String loanRequestId,
            String newStatus,
            String userEmail,
            String loanType,
            Double amount,
            Double interestRate,
            Integer termMonths,
            List<LoanInstallment> paymentPlan
    );
}
