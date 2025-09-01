package co.com.pragma.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void shouldBuildUserCorrectly() {
        LocalDate birthDate = LocalDate.of(1995, 5, 20);

        User user = User.builder()
                .documentNumber("123456")
                .name("Santiago")
                .lastName("Test")
                .birthDate(birthDate)
                .address("Calle Falsa 123")
                .phone("3001234567")
                .email("test@example.com")
                .baseSalary(BigDecimal.valueOf(5000))
                .build();

        assertThat(user.getDocumentNumber()).isEqualTo("123456");
        assertThat(user.getName()).isEqualTo("Santiago");
        assertThat(user.getLastName()).isEqualTo("Test");
        assertThat(user.getBirthDate()).isEqualTo(birthDate);
        assertThat(user.getAddress()).isEqualTo("Calle Falsa 123");
        assertThat(user.getPhone()).isEqualTo("3001234567");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getBaseSalary()).isEqualByComparingTo("5000");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        User user1 = User.builder()
                .documentNumber("123")
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .documentNumber("123")
                .email("test@example.com")
                .build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void shouldTestToBuilder() {
        User user = User.builder()
                .documentNumber("111")
                .name("Santiago")
                .build();

        User modified = user.toBuilder()
                .name("Felipe")
                .build();

        assertThat(modified.getDocumentNumber()).isEqualTo("111");
        assertThat(modified.getName()).isEqualTo("Felipe");
    }

    @Test
    void shouldTestNoArgsAndAllArgsConstructors() {
        User emptyUser = new User();
        emptyUser.setName("Santiago");
        assertThat(emptyUser.getName()).isEqualTo("Santiago");

        User fullUser = new User("222", "Felipe", "test",
                LocalDate.of(1990, 1, 1), "Av Siempre Viva",
                "3111111111", "test@example.com",
                BigDecimal.valueOf(7000));

        assertThat(fullUser.getName()).isEqualTo("Felipe");
        assertThat(fullUser.getEmail()).isEqualTo("test@example.com");
    }
}
