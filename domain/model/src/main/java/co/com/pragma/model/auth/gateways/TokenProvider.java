package co.com.pragma.model.auth.gateways;

import co.com.pragma.model.auth.Auth;

public interface TokenProvider {
    String generateToken(Auth auth);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
}
