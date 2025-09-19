package co.com.pragma.r2dbc.role.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("roles")
public class RoleEntity {
    @Id
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;
}