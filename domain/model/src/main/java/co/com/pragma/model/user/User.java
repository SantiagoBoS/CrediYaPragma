package co.com.pragma.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String documentNumber;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phone;
    private String email;
    private BigDecimal baseSalary;
    private String password;
    private String role;
}
