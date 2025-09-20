package co.com.pragma.model.loan.loannotification.gateways;

import reactor.core.publisher.Mono;

public interface NotificationServiceGateway {
    Mono<Void> sendLoanStatusUpdateNotification(String loanRequestId, String newStatus, String userEmail);
}
