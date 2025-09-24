package co.com.pragma.api.loan.dto;

import co.com.pragma.model.loan.capacity.LoanInstallment;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoanCapacityCalculationResponseDTO {
    private String loanRequestId;
    private String decision; // APPROVED, REJECTED, MANUAL_REVIEW
    private List<LoanInstallment> paymentPlan;
}