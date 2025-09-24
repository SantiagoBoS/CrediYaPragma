package co.com.pragma.api.loan.dto;

import co.com.pragma.model.loan.capacity.LoanInstallment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanCapacityCalculationResponseDTO {
    private String loanRequestId;
    private String decision; // APPROVED, REJECTED, MANUAL_REVIEW
    private List<LoanInstallment> paymentPlan;
}