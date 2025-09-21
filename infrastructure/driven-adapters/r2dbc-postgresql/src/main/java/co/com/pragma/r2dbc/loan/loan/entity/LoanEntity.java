package co.com.pragma.r2dbc.loan.loan.entity;

import co.com.pragma.model.loan.constants.RequestStatus;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("loan_requests")
public class LoanEntity {
    @Id
    private Long id;

    @Column("public_id")
    private UUID publicId;

    @Column("client_document")
    private String clientDocument;

    @Column("amount")
    private Double amount;

    @Column("term_months")
    private Integer termMonths;

    @Column("loan_type")
    private String loanType;

    @Column("advisor_id")
    private String advisorId;

    @Column("status")
    private RequestStatus status = RequestStatus.PENDING_REVIEW;

    @Column("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
