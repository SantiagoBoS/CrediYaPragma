package co.com.pragma.r2dbc.loan.loan.entity;

import co.com.pragma.model.loan.constants.RequestStatus;
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

    @Column(name = "advisor_id")
    private String advisorId;

    @Column(name = "status")
    private RequestStatus status = RequestStatus.PENDING_REVIEW;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedDate;
}
