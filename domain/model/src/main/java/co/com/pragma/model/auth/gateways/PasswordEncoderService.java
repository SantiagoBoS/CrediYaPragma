package co.com.pragma.model.auth.gateways;

public interface PasswordEncoderService {
    boolean matches(String rawPassword, String encodedPassword);
    String encode(String rawPassword);
}
