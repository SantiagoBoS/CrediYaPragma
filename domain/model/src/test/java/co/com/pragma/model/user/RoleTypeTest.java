package co.com.pragma.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTypeTest {

    private RoleType role1;
    private RoleType role2;
    private RoleType role3;
    private RoleType role4;
    private RoleType roleTest;

    @BeforeEach
    void setUp() {
        role1 = new RoleType(1L, "ADMIN", "Administrador del sistema");
        role2 = new RoleType(1L, "ADMIN", "Administrador del sistema");
        role3 = new RoleType(2L, "CLIENT", "Usuario cliente");
        role4 = new RoleType(3L, "ADVISOR", "Asesor de préstamos");
        roleTest = new RoleType(4L, "TEST", "Rol de prueba");
    }

    @Test
    void shouldBuildRoleTypeWithBuilder() {
        assertThat(role1.getId()).isEqualTo(1L);
        assertThat(role1.getCode()).isEqualTo("ADMIN");
        assertThat(role1.getDescription()).isEqualTo("Administrador del sistema");
    }

    @Test
    void shouldSetAndGetFieldsCorrectly() {
        assertThat(role3.getId()).isEqualTo(2L);
        assertThat(role3.getCode()).isEqualTo("CLIENT");
        assertThat(role3.getDescription()).isEqualTo("Usuario cliente");
    }

    @Test
    void shouldUseAllArgsConstructorCorrectly() {
        assertThat(role4.getId()).isEqualTo(3L);
        assertThat(role4.getCode()).isEqualTo("ADVISOR");
        assertThat(role4.getDescription()).isEqualTo("Asesor de préstamos");
    }

    @Test
    void shouldVerifyEqualsAndHashCode() {
        assertThat(role1).isEqualTo(role2);
        assertThat(role1).hasSameHashCodeAs(role2);
        assertThat(role1).isNotEqualTo(role3);
    }

    @Test
    void shouldVerifyToString() {
        String toString = roleTest.toString();
        assertThat(toString).contains("id=4");
        assertThat(toString).contains("code=TEST");
        assertThat(toString).contains("description=Rol de prueba");
    }
}
