package co.com.pragma.r2dbc.role.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;
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

    @Column("code")
    private String code;

    @Column("description")
    private String description;
}