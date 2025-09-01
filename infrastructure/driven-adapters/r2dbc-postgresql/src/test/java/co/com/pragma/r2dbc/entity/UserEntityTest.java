package co.com.pragma.r2dbc.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {
    @Test
    void shouldCreateUserEntityWithAllArgsConstructor() {
        LocalDate birthDate = LocalDate.of(1995, 5, 10);
        UserEntity entity = new UserEntity(
                "1",
                "123456",
                "Santiago",
                "Borrero",
                birthDate,
                "Calle 123",
                "3001234567",
                "santi@correo.com",
                BigDecimal.valueOf(3500000)
        );

        assertThat(entity.getId()).isEqualTo("1");
        assertThat(entity.getDocumentNumber()).isEqualTo("123456");
        assertThat(entity.getName()).isEqualTo("Santiago");
        assertThat(entity.getLastName()).isEqualTo("Borrero");
        assertThat(entity.getBirthDate()).isEqualTo(birthDate);
        assertThat(entity.getAddress()).isEqualTo("Calle 123");
        assertThat(entity.getPhone()).isEqualTo("3001234567");
        assertThat(entity.getEmail()).isEqualTo("santi@correo.com");
        assertThat(entity.getBaseSalary()).isEqualTo(BigDecimal.valueOf(3500000));
    }

    @Test
    void shouldUseNoArgsConstructorAndSetters() {
        UserEntity entity = new UserEntity();
        entity.setId("2");
        entity.setDocumentNumber("654321");
        entity.setName("Andrés");
        entity.setLastName("Suarez");

        assertThat(entity.getId()).isEqualTo("2");
        assertThat(entity.getDocumentNumber()).isEqualTo("654321");
        assertThat(entity.getName()).isEqualTo("Andrés");
        assertThat(entity.getLastName()).isEqualTo("Suarez");
    }

    @Test
    void shouldValidateEqualsAndHashCode() {
        UserEntity e1 = new UserEntity("1", "123", "Santiago", "Borrero",
                LocalDate.now(), "Calle 123", "3001234567", "santi@correo.com", BigDecimal.TEN);

        UserEntity e2 = new UserEntity("1", "123", "Santiago", "Borrero",
                LocalDate.now(), "Calle 123", "3001234567", "santi@correo.com", BigDecimal.TEN);

        assertThat(e1).isEqualTo(e2);
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
    }

    @Test
    void shouldHaveToStringImplementation() {
        UserEntity entity = new UserEntity();
        entity.setId("10");
        assertThat(entity.toString()).contains("10");
    }
}
