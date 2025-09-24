package co.com.pragma.model.loan.capacity;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CapacityResult {
    private String decision;
    private List<LoanInstallment> paymentPlan;
}