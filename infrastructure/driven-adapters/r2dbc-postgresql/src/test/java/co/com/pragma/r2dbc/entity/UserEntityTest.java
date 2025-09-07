package co.com.pragma.r2dbc.entity;

import co.com.pragma.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity(
                "1",
                "123456",
                "Santiago",
                "Borrero",
                LocalDate.of(1995, 5, 10),
                "Calle 123",
                "3001234567",
                "santi@correo.com",
                BigDecimal.valueOf(3500000)
        );
    }

    @Test
    void shouldCreateUserEntityWithAllArgsConstructor() {
        assertThat(userEntity.getId()).isEqualTo("1");
        assertThat(userEntity.getDocumentNumber()).isEqualTo("123456");
        assertThat(userEntity.getName()).isEqualTo("Santiago");
        assertThat(userEntity.getLastName()).isEqualTo("Borrero");
        assertThat(userEntity.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 10));
        assertThat(userEntity.getAddress()).isEqualTo("Calle 123");
        assertThat(userEntity.getPhone()).isEqualTo("3001234567");
        assertThat(userEntity.getEmail()).isEqualTo("santi@correo.com");
        assertThat(userEntity.getBaseSalary()).isEqualTo(BigDecimal.valueOf(3500000));
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        UserEntity testUserOne = userEntity;
        UserEntity testUserTwo = userEntity;
        assertThat(testUserOne).isEqualTo(testUserTwo);
        assertThat(testUserOne.hashCode()).isEqualTo(testUserTwo.hashCode());
    }

    @Test
    void shouldHaveToStringImplementation() {
        userEntity.setId("10");
        assertThat(userEntity.toString()).contains("10");
    }
}
