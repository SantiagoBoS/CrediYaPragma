package co.com.pragma.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Auth {
    private String document;
    private String email;
    private String password;
    private String role;
}
