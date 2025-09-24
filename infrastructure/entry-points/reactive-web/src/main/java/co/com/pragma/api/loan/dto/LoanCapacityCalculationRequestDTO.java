package co.com.pragma.api.loan.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanCapacityCalculationRequestDTO {
    private String userId;
    private BigDecimal income;
    private Double loanAmount;
    private Double annualInterestRate;
    private Integer termInMonths;
}