package co.com.pragma.model.loan.capacity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LoanInstallment {
    private Integer month;
    private BigDecimal capitalPayment;
    private BigDecimal interestPayment;
    private BigDecimal remainingBalance;
}