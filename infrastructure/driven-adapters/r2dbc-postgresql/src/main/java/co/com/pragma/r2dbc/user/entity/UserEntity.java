package co.com.pragma.r2dbc.user.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    private String id;

    @Column("document_number")
    private String documentNumber;

    @Column("name")
    private String name;

    @Column("last_name")
    private String lastName;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("password")
    private String password;

    @Column("role")
    private String role;
}
