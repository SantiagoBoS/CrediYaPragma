package co.com.pragma.usecase.loan;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUpdateUseCase {
    private final LoanUpdateRepository loanUpdateRepository;
    private final NotificationServiceGateway notificationServiceGateway;
    private final UserRepository userRepository;

    public Mono<LoanRequest> updateLoanStatus(String publicId, String newStatus, String advisorId) {
        return loanUpdateRepository.updateStatus(publicId, newStatus, advisorId)
                .flatMap(updatedLoan -> {
                    // Solo enviar notificación para estados finales
                    if ("APPROVED".equalsIgnoreCase(newStatus) || "REJECTED".equalsIgnoreCase(newStatus)) {
                        return getUserEmail(updatedLoan.getClientDocument())
                                .flatMap(userEmail -> notificationServiceGateway
                                        .sendLoanStatusUpdateNotification(publicId, newStatus, userEmail, updatedLoan.getLoanType())
                                        .thenReturn(updatedLoan)
                                )
                                .switchIfEmpty(Mono.error(new RuntimeException(AppMessages.LOAN_EMAIL_NOT_FOUND + updatedLoan.getClientDocument())));
                    }
                    return Mono.just(updatedLoan);
                });
    }

    private Mono<String> getUserEmail(String clientDocument) {
        return userRepository.findByDocumentNumber(clientDocument)
                .map(User::getEmail)
                .onErrorResume(error -> Mono.empty());
    }
}
