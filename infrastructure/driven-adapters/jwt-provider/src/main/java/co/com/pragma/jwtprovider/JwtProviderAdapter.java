package co.com.pragma.jwtprovider;

import co.com.pragma.model.auth.Auth;
import co.com.pragma.model.auth.gateways.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProviderAdapter implements TokenProvider {
    private final JwtConfig jwtConfig;
    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        //this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    @Override
    public String generateToken(Auth auth) {
        // Genera un token JWT con el email y rol del usuario
        return Jwts.builder()
                .setSubject(auth.getEmail())
                .claim("role", auth.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMs()))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        // Extrae el email del usuario del token JWT
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public String getRoleFromToken(String token) {
        // Extrae el rol del usuario del token JWT
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }
}