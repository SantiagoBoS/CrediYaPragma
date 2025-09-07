package co.com.pragma.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private User buildBaseUser() {
        return User.builder()
                .documentNumber("123456")
                .name("Santiago")
                .lastName("Test")
                .birthDate(LocalDate.of(1995, 5, 20))
                .address("Calle Falsa 123")
                .phone("3001234567")
                .email("test@example.com")
                .baseSalary(BigDecimal.valueOf(5000))
                .build();
    }

    @Test
    void shouldBuildUserCorrectly() {
        User user = buildBaseUser();

        assertThat(user.getDocumentNumber()).isEqualTo("123456");
        assertThat(user.getName()).isEqualTo("Santiago");
        assertThat(user.getLastName()).isEqualTo("Test");
        assertThat(user.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 20));
        assertThat(user.getAddress()).isEqualTo("Calle Falsa 123");
        assertThat(user.getPhone()).isEqualTo("3001234567");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getBaseSalary()).isEqualByComparingTo("5000");
    }

    @Test
    void shouldTestEqualsAndHashCode() {
        User user1 = buildBaseUser();
        User user2 = buildBaseUser();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    void shouldTestToBuilder() {
        User modified = buildBaseUser().toBuilder()
                .name("Felipe")
                .build();

        assertThat(modified.getDocumentNumber()).isEqualTo("123456");
        assertThat(modified.getName()).isEqualTo("Felipe");
    }

    @Test
    void shouldTestNoArgsAndAllArgsConstructors() {
        User emptyUser = new User();
        emptyUser.setName("Santiago");
        assertThat(emptyUser.getName()).isEqualTo("Santiago");
        User modified = buildBaseUser().toBuilder()
                .name("Felipe")
                .email("test@example.com")
                .build();

        assertThat(modified.getName()).isEqualTo("Felipe");
        assertThat(modified.getEmail()).isEqualTo("test@example.com");
    }
}
