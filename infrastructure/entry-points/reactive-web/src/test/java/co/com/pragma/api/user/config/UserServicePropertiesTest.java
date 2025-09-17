package co.com.pragma.api.user.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServicePropertiesTest {

    @Test
    void shouldSetAndGetBaseUrl() {
        UserServiceProperties properties = new UserServiceProperties();
        String url = "http://localhost:8080/users";
        properties.setBaseUrl(url);

        assertThat(properties.getBaseUrl()).isEqualTo(url);
    }
}