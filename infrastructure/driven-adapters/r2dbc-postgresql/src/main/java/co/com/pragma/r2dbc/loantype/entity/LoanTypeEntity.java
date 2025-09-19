package co.com.pragma.r2dbc.loantype.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Column;
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

    @Column(name = "interest_rate")
    private String interestRate;
}