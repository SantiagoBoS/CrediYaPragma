package co.com.pragma.r2dbc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("loan_requests")
public class LoanEntity {
    @Id
    private String id;

    @Column(name = "client_document")
    private String clientDocument;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "term_months")
    private Integer termMonths;

    @Column(name = "loan_type")
    private String loanType;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
