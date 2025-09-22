package co.com.pragma.model.loan;

import co.com.pragma.model.loan.constants.RequestStatus;
import lombok.*;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanRequest {
    private UUID publicId;
    private String clientDocument;
    private Double amount;
    private Integer termMonths;
    private String loanType;
    private RequestStatus status;
    private String advisorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}