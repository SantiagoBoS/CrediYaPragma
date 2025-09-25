package co.com.pragma.usecase.loan;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.loan.gateways.LoanUpdateRepository;
import co.com.pragma.model.reports.gateways.ReportRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.usecase.loan.notification.LoanNotificationService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoanUpdateUseCase {

    private final LoanUpdateRepository loanUpdateRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanNotificationService loanNotificationService;
    private final UserRepository userRepository;
    private final LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;
    private final ReportRepository reportRepository;

    public Mono<LoanRequest> updateLoanStatus(String publicId, String newStatus, String advisorId) {
        return loanUpdateRepository.updateStatus(publicId, newStatus, advisorId)
            .flatMap(updatedLoan -> {
                if (RequestStatus.APPROVED.toString().equalsIgnoreCase(newStatus) || RequestStatus.REJECTED.toString().equalsIgnoreCase(newStatus)) {
                    //obtener el usuario
                    return userRepository.findByDocumentNumber(updatedLoan.getClientDocument())
                        // Obtener datos del LoanType
                        .flatMap(user -> loanTypeRepository.findByCode(updatedLoan.getLoanType())
                            .flatMap(loanType -> {
                                if (RequestStatus.APPROVED.toString().equalsIgnoreCase(newStatus)) {
                                    //Para validar en Dynamo
                                    return reportRepository.incrementCounter()
                                        .then(loanCalculateCapacityUseCase.execute(
                                            updatedLoan.getClientDocument(),
                                            user.getBaseSalary(),
                                            updatedLoan.getAmount(),
                                            loanType.getInterestRate(),
                                            updatedLoan.getTermMonths()
                                        ))
                                        .flatMap(capacityResult ->
                                            loanNotificationService.notifyApproved(
                                                updatedLoan,
                                                user.getEmail(),
                                                loanType.getCode(),
                                                loanType.getInterestRate(),
                                                capacityResult.getPaymentPlan()
                                            ).thenReturn(updatedLoan)
                                        );
                                }
                                return loanNotificationService.notifyRejected(
                                    updatedLoan,
                                    user.getEmail(),
                                    loanType.getCode()
                                ).thenReturn(updatedLoan);
                            })
                        ).switchIfEmpty(Mono.error(new RuntimeException(AppMessages.LOAN_EMAIL_NOT_FOUND + updatedLoan.getClientDocument())));
                }
                return Mono.just(updatedLoan);
            });
    }
}
