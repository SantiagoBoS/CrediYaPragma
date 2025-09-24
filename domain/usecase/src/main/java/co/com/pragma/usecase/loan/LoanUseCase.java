package co.com.pragma.usecase.loan;

import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.capacity.LoanInstallment;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.gateways.LoanRepository;
import co.com.pragma.model.loan.gateways.LoanTypeRepository;
import co.com.pragma.model.sqsnotification.gateways.NotificationServiceGateway;
import co.com.pragma.model.user.gateways.UserDocumentRepository;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class LoanUseCase {
    private final LoanRepository loanRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;
    private final UserRepository userRepository;
    private final NotificationServiceGateway notificationServiceGateway;

    public Mono<LoanRequest> register(LoanRequest loanRequest, String token) {
        return userDocumentRepository.existsByDocumentToken(loanRequest.getClientDocument(), token)
                .then(loanTypeRepository.findByCode(loanRequest.getLoanType())
                        .switchIfEmpty(Mono.error(new BusinessException(AppMessages.LOAN_TYPE_INVALID.getMessage())))
                        .flatMap(loanType -> {
                            if (Boolean.TRUE.equals(loanType.getAutomaticValidation())) {
                                // Flujo automático: calcular capacidad y enviar notificación
                                return userRepository.findByDocumentNumber(loanRequest.getClientDocument())
                                        .flatMap(user ->
                                                loanCalculateCapacityUseCase.execute(
                                                                loanRequest.getClientDocument(),
                                                                user.getBaseSalary(),
                                                                loanRequest.getAmount(),
                                                                loanType.getInterestRate(),
                                                                loanRequest.getTermMonths()
                                                        )
                                                        .flatMap(capacityResult -> {
                                                            // Construir LoanRequest con estado calculado
                                                            LoanRequest loanWithDecision = loanRequest.toBuilder()
                                                                    .publicId(UUID.randomUUID())
                                                                    .status(RequestStatus.valueOf(capacityResult.getDecision()))
                                                                    .build();

                                                            List<LoanInstallment> paymentPlan = capacityResult.getPaymentPlan();
                                                            // Guardar solicitud
                                                            return loanRepository.save(loanWithDecision)
                                                                    .flatMap(savedLoan ->
                                                                            // Enviar notificación con el resultado final
                                                                            notificationServiceGateway.sendLoanStatusUpdateNotification(
                                                                                            savedLoan.getPublicId().toString(),
                                                                                            savedLoan.getStatus().toString(),
                                                                                            user.getEmail(),
                                                                                            loanType.getCode(),
                                                                                            savedLoan.getAmount(),
                                                                                            loanType.getInterestRate(),
                                                                                            savedLoan.getTermMonths(),
                                                                                            paymentPlan
                                                                                    )
                                                                                    .thenReturn(savedLoan)
                                                                    );
                                                        })
                                        );
                            } else {
                                // Si no hay validación automática, solo asignar ID y guardar
                                LoanRequest loanWithId = loanRequest.toBuilder()
                                        .publicId(UUID.randomUUID())
                                        .build();
                                return loanRepository.save(loanWithId);
                            }
                        })
                );
    }
}
