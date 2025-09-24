package co.com.pragma.api.loan.handler;

import co.com.pragma.api.loan.dto.LoanCapacityCalculationRequestDTO;
import co.com.pragma.api.loan.dto.LoanCapacityCalculationResponseDTO;
import co.com.pragma.model.loan.capacity.CapacityResult;
import co.com.pragma.usecase.loan.LoanCalculateCapacityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanCapacityHandler {

    private final LoanCalculateCapacityUseCase loanCalculateCapacityUseCase;

    public Mono<ServerResponse> calculateCapacity(ServerRequest request) {
        return request.bodyToMono(LoanCapacityCalculationRequestDTO.class)
            .flatMap(dto -> loanCalculateCapacityUseCase.execute(
                    dto.getUserId(),
                    dto.getIncome(),
                    dto.getLoanAmount(),
                    dto.getAnnualInterestRate(),
                    dto.getTermInMonths()
            ).map(result -> new Object[]{dto, result}))
            .flatMap(tuple -> {
                LoanCapacityCalculationRequestDTO dto = (LoanCapacityCalculationRequestDTO) tuple[0];
                var result = (CapacityResult) tuple[1];

                return ServerResponse.ok().bodyValue(
                        LoanCapacityCalculationResponseDTO.builder()
                                .loanRequestId(dto.getUserId())
                                .decision(result.getDecision())
                                .paymentPlan(result.getPaymentPlan())
                                .build()
                );
            });
    }
}