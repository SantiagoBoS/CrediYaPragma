package co.com.pragma.r2dbc.loan.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("loan_types")
public class LoanTypeEntity {
    @Id
    private Long id;
    private String code;
    private String description;
}