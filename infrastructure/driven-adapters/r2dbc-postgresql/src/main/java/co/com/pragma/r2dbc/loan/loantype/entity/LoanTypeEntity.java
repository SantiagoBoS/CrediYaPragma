package co.com.pragma.r2dbc.loan.loantype.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("loan_types")
public class LoanTypeEntity {
    @Id
    private Long id;
    private String code;
    private String description;

    @Column("interest_rate")
    private String interestRate;
}