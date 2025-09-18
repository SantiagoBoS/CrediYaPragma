package co.com.pragma.api.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanListResponseDTO {
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String fullName;
    private String loanType;
    private BigDecimal interestRate;
    private String requestStatus;
    private BigDecimal monthlyPayment;
}
