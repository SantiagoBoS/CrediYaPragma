package co.com.pragma.model.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long totalApprovedLoans;
    private Double totalApprovedAmount;
}