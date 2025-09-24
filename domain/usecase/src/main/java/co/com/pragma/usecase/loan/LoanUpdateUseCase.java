package co.com.pragma.usecase.loan;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class LoanUpdateUseCase {

    private final LoanUpdateRepository loanUpdateRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final NotificationServiceGateway notificationServiceGateway;
    private final UserRepository userRepository;
    private final LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;

    public Mono<LoanRequest> updateLoanStatus(String publicId, String newStatus, String advisorId) {
        return loanUpdateRepository.updateStatus(publicId, newStatus, advisorId)
            .flatMap(updatedLoan -> {
                // Solo actuar si el estado es final
                if (RequestStatus.APPROVED.toString().equalsIgnoreCase(newStatus) ||
                        RequestStatus.REJECTED.toString().equalsIgnoreCase(newStatus)) {

                    // Obtener usuario
                    return userRepository.findByDocumentNumber(updatedLoan.getClientDocument())
                        .flatMap(user ->

                            // Obtener datos del LoanType
                            loanTypeRepository.findByCode(updatedLoan.getLoanType())
                                .switchIfEmpty(Mono.error(new RuntimeException(AppMessages.LOAN_NOT_FOUND + ": " + updatedLoan.getLoanType())))
                                .flatMap(loanType -> {

                                    // Si es APPROVED, calcular plan de pagos
                                    if (RequestStatus.APPROVED.toString().equalsIgnoreCase(newStatus)) {
                                        return loanCalculateCapacityUseCase.execute(
                                                updatedLoan.getClientDocument(),
                                                user.getBaseSalary(),
                                                updatedLoan.getAmount(),
                                                loanType.getInterestRate(),
                                                updatedLoan.getTermMonths()
                                            )
                                            .flatMap(capacityResult -> {
                                                List<LoanInstallment> paymentPlan = capacityResult.getPaymentPlan();
                                                return notificationServiceGateway.sendLoanStatusUpdateNotification(
                                                        updatedLoan.getPublicId().toString(),
                                                        updatedLoan.getStatus().toString(),
                                                        user.getEmail(),
                                                        updatedLoan.getLoanType(),
                                                        updatedLoan.getAmount(),
                                                        loanType.getInterestRate(),
                                                        updatedLoan.getTermMonths(),
                                                        paymentPlan
                                                    ).thenReturn(updatedLoan);
                                            });
                                    }

                                    // Si es REJECTED, no generar plan de pagos
                                    return notificationServiceGateway.sendLoanStatusUpdateNotification(
                                            updatedLoan.getPublicId().toString(),
                                            updatedLoan.getStatus().toString(),
                                            user.getEmail(),
                                            updatedLoan.getLoanType(),
                                            null,
                                            null,
                                            null,
                                            List.of()
                                        ).thenReturn(updatedLoan);
                                })
                        )
                        .switchIfEmpty(Mono.error(new RuntimeException(AppMessages.LOAN_EMAIL_NOT_FOUND + updatedLoan.getClientDocument())));
                }

                return Mono.just(updatedLoan);
            });
    }
}
