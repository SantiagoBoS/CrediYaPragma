package co.com.pragma.api.loan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusUpdateDTO {

    @NotBlank(message = "El estado es requerido")
    @Pattern(regexp = "APPROVED|REJECTED", message = "El estado debe ser APPROVED o REJECTED")
    private String status;
}