package co.com.pragma.model.loan;

import lombok.*;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanRequest {
    private String clientDocument;
    private BigDecimal amount;
    private Integer termMonths;
    private String loanType;
    private RequestStatus status;
    private LocalDateTime createdAt;
}