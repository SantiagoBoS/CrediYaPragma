package co.com.pragma.model.loan;

import co.com.pragma.model.loan.constants.RequestStatus;
import lombok.*;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanRequest {
    private String clientDocument;
    private Double amount;
    private Integer termMonths;
    private String loanType;
    private RequestStatus status;
    private LocalDateTime createdAt;
}