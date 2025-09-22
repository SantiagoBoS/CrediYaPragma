package co.com.pragma.model.loan;

import co.com.pragma.model.loan.constants.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanList {
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String fullName;
    private String loanType;
    private BigDecimal interestRate;
    private RequestStatus requestStatus;
    private BigDecimal baseSalary;
    private BigDecimal monthlyPayment;
}
