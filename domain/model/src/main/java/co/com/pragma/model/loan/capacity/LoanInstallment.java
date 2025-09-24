package co.com.pragma.model.loan.capacity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanInstallment {
    private Integer month;
    private BigDecimal capitalPayment;
    private BigDecimal interestPayment;
    private BigDecimal remainingBalance;
}