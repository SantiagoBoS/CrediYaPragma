package co.com.pragma.api.dto;

import co.com.pragma.model.loan.constants.RequestStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDTO {
    @NotBlank(message = "El documento no puede estar vacio")
    private String clientDocument;

    @NotNull(message = "El monto no puede estar vacio")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    private Double amount;

    @NotNull(message = "El termino no puede estar vacio")
    @Min(value = 1, message = "El termino debe ser mayor a 0")
    private Integer termMonths;

    @NotBlank(message = "Se requiere el tipo de prestamo")
    @Pattern(regexp = "PERSONAL|MORTGAGE|CAR", message = "Tipo de prestamo invalido")
    private String loanType;

    private RequestStatus status;
    private LocalDateTime createdAt;
}
