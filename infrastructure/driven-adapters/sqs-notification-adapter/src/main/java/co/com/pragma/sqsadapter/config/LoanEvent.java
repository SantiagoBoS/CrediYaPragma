package co.com.pragma.sqsadapter.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanEvent {
    private String id;
    private String status;
    private Double amount;
}
