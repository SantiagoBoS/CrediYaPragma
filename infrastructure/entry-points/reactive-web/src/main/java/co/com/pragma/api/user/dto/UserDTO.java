package co.com.pragma.api.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    @NotBlank(message = "El documento no puede estar vacío")
    private String documentNumber;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    private LocalDate birthDate;

    private String address;

    @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String phone;

    @Email(message = "Correo electrónico inválido")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    private String email;

    @NotNull(message = "Salario base no puede ser nulo")
    @Min(value = 0, message = "Salario base no puede ser menor a 0")
    @Max(value = 15000000, message = "Salario base no puede ser mayor a 15.000.000")
    private BigDecimal baseSalary;
}