package co.com.pragma.r2dbc.loanlist.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("loan_list")
public class LoanListEntity {
    @Id
    private String id;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String firstName;
    private String lastName;
    private String loanType;
    private BigDecimal interestRate;
    private String requestStatus;
    private BigDecimal baseSalary;
    private BigDecimal monthlyPayment;
}
