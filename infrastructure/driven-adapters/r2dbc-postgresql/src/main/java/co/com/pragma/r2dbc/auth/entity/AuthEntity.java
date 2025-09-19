package co.com.pragma.r2dbc.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("auth")
public class AuthEntity {
    @Id
    private Long id;

    @Column("document")
    private String document;

    @Column("email")
    private String email;

    @Column("password")
    private String password;

    @Column("role")
    private String role;
}
